import SwiftUI

struct ProfileView: View {
    @StateObject private var viewModel: ProfileViewModel
    @EnvironmentObject var appViewModel: AppViewController

    @State private var profileImage: UIImage?
    @State private var showImagePicker = false
    @State private var showLogoutConfirmation = false
    @State private var showEditProfile = false

    init(userId: Int) {
        _viewModel = StateObject(wrappedValue: ProfileViewModel(userId: userId))
    }

    var body: some View {
        NavigationView {
            Group {
                if viewModel.isLoading {
                    ProgressView("Загрузка профиля...")
                } else if let profile = viewModel.profileResponse {
                    ScrollView {
                        VStack(alignment: .center) {
                            // Аватар профиля
                            AsyncImage(url: URL(string: profile.avatar ?? "")) { image in
                                image.resizable()
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                    .clipShape(Circle())
                            } placeholder: {
                                Image("defaultProfile")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                    .clipShape(Circle())
                            }
                            .padding()

                            Text(profile.username)
                                .font(.title)
                                .fontWeight(.bold)
                                .multilineTextAlignment(.center)

                            Text(profile.bio ?? "")
                                .font(.subheadline)
                                .foregroundColor(.gray)
                                .multilineTextAlignment(.center)
                                .padding(.bottom)

                            if profile.posts.isEmpty {
                                Text("Нет постов")
                                    .foregroundColor(.gray)
                            } else {
                                LazyVGrid(columns: Array(repeating: .init(.flexible()), count: 3), spacing: 10) {
                                    ForEach(profile.posts) { post in
                                        AsyncImage(url: URL(string: post.image)) { image in
                                            image.resizable()
                                                .scaledToFit()
                                                .frame(height: 120)
                                                .cornerRadius(8)
                                        } placeholder: {
                                            ProgressView()
                                        }
                                    }
                                }
                                .padding()
                            }
                        }
                        .padding()
                    }
                } else if let error = viewModel.error {
                    VStack {
                        Text("Ошибка загрузки профиля")
                            .foregroundColor(.red)
                        Text(error.localizedDescription)
                            .foregroundColor(.gray)
                        Button("Повторить") {
                            viewModel.loadProfile()
                        }
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Menu {
                        
                        Button(action: {
                            showLogoutConfirmation = true
                        }) {
                            Text("Выйти")
                                .foregroundColor(.red)
                        }
                    } label: {
                        Image(systemName: "ellipsis")
                            .font(.title)
                    }
                }
            }
            .actionSheet(isPresented: $showLogoutConfirmation) {
                ActionSheet(
                    title: Text("Вы уверены, что хотите выйти?"),
                    buttons: [
                        .destructive(Text("Выйти")) {
                            appViewModel.logout()
                        },
                        .cancel(),
                    ]
                )
            }
            
            }
            .onAppear {
                viewModel.loadProfile()
            }
        }
}
