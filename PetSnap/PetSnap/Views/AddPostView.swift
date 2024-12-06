import AVFoundation
import Photos
import PhotosUI
import SwiftUI

struct AddPostView: View {
    @State private var selectedImage: UIImage?
    @State private var postText: String = ""
    @State private var showImagePicker = false
    @State private var showCamera = false
    @State private var showPermissionDeniedAlert = false
    @State private var isPhotoSelected = false
    @State private var galleryPhotos: [UIImage] = []
    @StateObject private var postViewModel = PostViewController()

    var body: some View {
        NavigationView {
            VStack {
                if !isPhotoSelected {
                    ScrollView(.vertical) {
                        LazyVGrid(columns: [
                            GridItem(.flexible()),

                            GridItem(.flexible()),

                            GridItem(.flexible()),

                        ], spacing: 2) {
                            ForEach(galleryPhotos, id: \.self) { photo in

                                Image(uiImage: photo)

                                    .resizable()
                                    .scaledToFill()
                                    .frame(height: 120)
                                    .clipped()
                                    .onTapGesture {
                                        selectedImage = photo

                                        isPhotoSelected = true
                                    }
                            }
                        }
                    }

                    .onAppear(perform: loadGalleryPhotos)
                }

                // Если фото выбрано - показываем форму ввода

                if let image = selectedImage {
                    VStack {
                        Image(uiImage: image)

                            .resizable()
                            .scaledToFit()
                            .frame(height: 300)

                        TextEditor(text: $postText)

                            .frame(height: 150)
                            .border(Color.gray.opacity(0.2), width: 1)
                            .cornerRadius(8)
                            .padding()
                    }
                }

                Spacer()

                // Кнопка камеры

                Button(action: {
                    showCamera = true

                }) {
                    Image(systemName: "camera.fill")

                        .foregroundColor(.white)
                        .padding()
                        .background(Color.blue)
                        .clipShape(Circle())
                }

                .sheet(isPresented: $showCamera) {
                    ImagePicker(image: $selectedImage, sourceType: .camera)

                        .onChange(of: selectedImage) { _ in

                            isPhotoSelected = true
                        }
                }
            }

            .navigationTitle("Создание поста")
            .navigationBarItems(
                leading: isPhotoSelected ? Button("Назад") {
                    selectedImage = nil

                    isPhotoSelected = false

                } : nil,

                trailing:

                Button(action: createPost) {
                    HStack {
                        Image(systemName: "paperplane.fill")

                        Text("Опубликовать")
                    }

                    .padding(.horizontal, 12)
                    .padding(.vertical, 8)
                    .background(selectedImage != nil ? Color.blue : Color.gray.opacity(0.3))
                    .foregroundColor(selectedImage != nil ? .white : .gray)
                    .cornerRadius(20)
                    .font(.system(size: 14, weight: .semibold))
                }

                .disabled(selectedImage == nil)
            )
        }
    }

    func loadGalleryPhotos() {
        // Запрос доступа к галерее

        PHPhotoLibrary.requestAuthorization { status in

            if status == .authorized {
                let fetchOptions = PHFetchOptions()

                fetchOptions.sortDescriptors = [NSSortDescriptor(key: "creationDate", ascending: false)]

                let fetchResult = PHAsset.fetchAssets(with: .image, options: fetchOptions)

                DispatchQueue.global().async {
                    var images: [UIImage] = []

                    let imageManager = PHImageManager.default()

                    let requestOptions = PHImageRequestOptions()

                    requestOptions.isSynchronous = true

                    for index in 0 ..< min(fetchResult.count, 30) {
                        let asset = fetchResult.object(at: index)

                        imageManager.requestImage(for: asset,

                                                  targetSize: CGSize(width: 200, height: 200),

                                                  contentMode: .aspectFill,

                                                  options: requestOptions)
                        { image, _ in

                            if let img = image {
                                images.append(img)
                            }
                        }
                    }

                    DispatchQueue.main.async {
                        self.galleryPhotos = images
                    }
                }
            }
        }
    }

    private func checkCameraPermission() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            showCamera = true
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { granted in
                DispatchQueue.main.async {
                    if granted {
                        showCamera = true
                    } else {
                        showPermissionDeniedAlert = true
                    }
                }
            }
        case .denied, .restricted:
            showPermissionDeniedAlert = true
        @unknown default:
            break
        }
    }

    private func checkPhotoLibraryPermission() {
        let status = PHPhotoLibrary.authorizationStatus(for: .readWrite)
        switch status {
        case .authorized:
            showImagePicker = true
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization(for: .readWrite) { status in
                DispatchQueue.main.async {
                    if status == .authorized {
                        showImagePicker = true
                    } else {
                        showPermissionDeniedAlert = true
                    }
                }
            }
        case .denied, .restricted:
            showPermissionDeniedAlert = true
        case .limited:
            showImagePicker = true
        @unknown default:
            break
        }
    }

    private func openSettings() {
        guard let settingsUrl = URL(string: UIApplication.openSettingsURLString) else { return }

        if UIApplication.shared.canOpenURL(settingsUrl) {
            UIApplication.shared.open(settingsUrl)
        }
    }

    private func createPost() {
        if let image = selectedImage {
            postViewModel.createPost(image: image, text: postText) { post in
                if post != nil {
                    // Полный сброс состояния
                    selectedImage = nil
                    postText = ""
                    isPhotoSelected = false
                    galleryPhotos = [] // Очистка галереи

                    // Закрываем текущее представление
                } else {}
            }
        }
    }
}
