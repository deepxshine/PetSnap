import SwiftUI

struct UserPost: Identifiable {
    let id = UUID()
    let image: Image
}
import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var appViewModel: AppViewController

    @State private var profileImage: UIImage?
    @State private var showImagePicker = false
    @State private var bio: String = "Краткое описание о себе."
    @State private var username: String = "Имя пользователя"
    @State private var showLogoutConfirmation = false
    @State private var showEditProfile = false
    @State private var posts: [UserPost] = [] // Пустой массив постов для заглушки


    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .center) {
                    // Аватар профиля
                    if let profileImage = profileImage {
                        Image(uiImage: profileImage)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                            .padding()
                    } else {
                        Image("defaultProfile")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                            .padding()
                    }

                    // Имя пользователя
                    Text(username)
                        .font(.title)
                        .fontWeight(.bold)
                        .multilineTextAlignment(.center)

                    // Био
                    Text(bio)
                        .font(.subheadline)
                        .foregroundColor(.gray)
                        .multilineTextAlignment(.center)
                        .padding(.bottom)

                    // Проверка на наличие постов
                    if posts.isEmpty {
                        ProgressView("Загрузка постов...")
                            .progressViewStyle(CircularProgressViewStyle())
                            .padding()
                    } else {
                        LazyVGrid(columns: Array(repeating: .init(.flexible()), count: 3), spacing: 10) {
                            ForEach(posts) { post in
                                post.image
                                    .resizable()
                                    .scaledToFit()
                                    .frame(height: 120)
                                    .cornerRadius(8)
                            }
                        }
                        .padding()
                    }
                }
                .padding()


            Button(action: {
                appViewModel.logout() // Выход из системы
            }) {
                Text("Выход")
                    .font(.headline)
                    .padding()
                    .background(Color.red)
                    .foregroundColor(.white)
                    .cornerRadius(8)

            }
            .navigationTitle("Профиль")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Menu {
                        Button(action: {
                            showEditProfile = true
                        }) {
                            Text("Изменить профиль")
                        }
                        Button(action: {
                            showLogoutConfirmation = true
                        }) {
                            Text("Разлогиниться")
                                .foregroundColor(.red)
                        }
                    } label: {
                        Image(systemName: "ellipsis")
                            .font(.title)
                    }
                    .actionSheet(isPresented: $showLogoutConfirmation) {
                        ActionSheet(
                            title: Text("Вы уверены, что хотите выйти?"),
                            buttons: [
                                .destructive(Text("Разлогиниться")) {
                                    appViewModel.logout()
                                },
                                .cancel()
                            ]
                        )
                    }
                    .sheet(isPresented: $showEditProfile) {
                        EditProfileView(profileImage: $profileImage, username: $username)
                    }
                }
            }
        }
    }
}

struct EditProfileView: View {
    @Binding var profileImage: UIImage?
    @Binding var username: String
    @Environment(\.presentationMode) var presentationMode
    @State private var showImagePicker = false
    @State private var selectedImage: UIImage?

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Профиль")) {
                    // Изображение профиля
                    Button(action: {
                        showImagePicker.toggle()
                    }) {
                        HStack {
                            if let image = selectedImage ?? profileImage {
                                Image(uiImage: image)
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                    .clipShape(Circle())
                            } else {
                                Image(systemName: "person.crop.circle.fill")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                    .foregroundColor(.gray)
                            }
                            Text("Изменить изображение")
                                .font(.headline)
                        }
                    }

                    // Поле для ввода имени пользователя
                    TextField("Имя пользователя", text: $username)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                }

                Section {
                    Button(action: {
                        // Сохранить изменения и закрыть экран
                        if let selectedImage = selectedImage {
                            profileImage = selectedImage
                        }
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Text("Сохранить")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .cornerRadius(8)
                    }
                }
            }
            .navigationTitle("Редактировать профиль")
            .navigationBarItems(trailing: Button("Готово") {
                presentationMode.wrappedValue.dismiss()
            })
            .sheet(isPresented: $showImagePicker) {
                ImagePicker(image: $selectedImage)
            }
        }
    }
}

// Компонент для выбора изображения
struct ImagePicker: UIViewControllerRepresentable {
    @Binding var image: UIImage?

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}

    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        var parent: ImagePicker

        init(_ parent: ImagePicker) {
            self.parent = parent
        }

        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
            if let uiImage = info[.originalImage] as? UIImage {
                parent.image = uiImage
            }
            picker.dismiss(animated: true)
        }

        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            picker.dismiss(animated: true)
        }
    }
}

#Preview {
    ProfileView()
        .environmentObject(AppViewController())
}
