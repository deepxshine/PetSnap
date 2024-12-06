//
//  GalleryView.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 27.11.2024.
//

import PhotosUI
import SwiftUI

struct GalleryView: View {
    @State private var selectedItem: PhotosPickerItem? = nil
    @State private var selectedImage: Image? = nil
    @State private var isAccessGranted: Bool = false
    @State private var photos: [PHAsset] = []
    @State private var selectedPhoto: UIImage? = nil
    @State private var descriptionText: String = "" // Для хранения описания

    var body: some View {
        VStack {
            if isAccessGranted {
                // Отображение выбранного изображения
                if let selectedPhoto = selectedPhoto {
                    Image(uiImage: selectedPhoto)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 250)
                        .padding()

                    // Форма для ввода описания
                    TextField("Введите описание", text: $descriptionText)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .padding()
                }

                // Сетка с фото
                ScrollView {
                    LazyVGrid(columns: Array(repeating: GridItem(.flexible()), count: 3)) {
                        ForEach(photos, id: \.self) { asset in
                            Button(action: {
                                // Обработка выбора фото
                                selectedPhoto = getImage(asset: asset)
                            }) {
                                Image(uiImage: getImage(asset: asset))
                                    .resizable()
                                    .scaledToFit()
                                    .frame(height: 100)
                                    .cornerRadius(5)
                                    .padding(5)
                            }
                        }
                    }
                }

                // Кнопка выбора другого изображения
                PhotosPicker(
                    selection: $selectedItem,
                    matching: .images
                ) {
                    Text("Выбрать другое")
                }
                .onChange(of: selectedItem) { newItem in
                    Task {
                        guard let newItem = newItem else { return }
                        do {
                            if let data = try await newItem.loadTransferable(type: Data.self),
                               let uiImage = UIImage(data: data)
                            {
                                selectedImage = Image(uiImage: uiImage)
                                selectedPhoto = uiImage // Сохранить выбранное фото
                                descriptionText = "" // Сбросить описание при выборе нового фото
                            }
                        } catch {
                            print("Ошибка при загрузке изображения: \(error.localizedDescription)")
                        }
                    }
                }
            } else {
                Button("Запросить доступ к галерее") {
                    requestPhotoLibraryAccess()
                }
                .padding()
            }
        }
        .padding()
        .onAppear {
            checkPhotoLibraryAccess()
            fetchPhotos()
        }
    }

    private func checkPhotoLibraryAccess() {
        let status = PHPhotoLibrary.authorizationStatus()
        isAccessGranted = status == .authorized
        if isAccessGranted {
            fetchPhotos()
        }
    }

    private func requestPhotoLibraryAccess() {
        PHPhotoLibrary.requestAuthorization { status in
            DispatchQueue.main.async {
                isAccessGranted = status == .authorized
                if isAccessGranted {
                    fetchPhotos()
                }
            }
        }
    }

    private func fetchPhotos() {
        let fetchOptions = PHFetchOptions()
        fetchOptions.sortDescriptors = [NSSortDescriptor(key: "creationDate", ascending: false)]
        let fetchResult = PHAsset.fetchAssets(with: .image, options: fetchOptions)

        photos = fetchResult.objects(at: IndexSet(integersIn: 0 ..< fetchResult.count))
    }

    private func getImage(asset: PHAsset) -> UIImage {
        let imageManager = PHImageManager.default()
        var uiImage = UIImage()
        let options = PHImageRequestOptions()
        options.isSynchronous = true

        imageManager.requestImage(for: asset, targetSize: CGSize(width: 300, height: 300), contentMode: .aspectFill, options: options) { image, _ in
            if let img = image {
                uiImage = img
            }
        }
        return uiImage
    }
}
