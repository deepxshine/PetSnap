import SwiftUI

struct FollowView: View {
    @StateObject var followViewModel = FollowViewController()
    @State private var selection = "followers"

    var body: some View {
        VStack {
            HStack {
                Button(action: {
                    followViewModel.loadInitialFollowersAndFollowings()
                    selection = "followers"
                }) {
                    Text("Подписчики")
                        .font(.title2)
                        .padding(.vertical, 10)
                        .padding(.horizontal, 20)
                        .foregroundColor(selection == "followers" ? .black : .gray)
                        .cornerRadius(10)
                }
                Spacer()
                Button(action: {
                    followViewModel.loadInitialFollowersAndFollowings()
                    selection = "followings"
                }) {
                    Text("Подписки")
                        .font(.title2)
                        .padding(.vertical, 10)
                        .padding(.horizontal, 20)
                        .foregroundColor(selection == "followings" ? .black : .gray)
                        .cornerRadius(10)
                }
            }
            .padding()
            .cornerRadius(10)

            Spacer()

            if selection == "followers" {
                List {
                    if let userId = followViewModel.userId {
                        Section(header: Text("Подписчики")) {
                            ForEach(followViewModel.followers, id: \.id) { follower in

                                UserRow(follower: follower, userId: userId)
                            }
                        }

                    } else {
                        Section(header: Text("Подписчики")) {
                            Text("Загрузка...")

                                .foregroundColor(.gray)
                        }
                    }
                }

                .refreshable {
                    followViewModel.loadInitialFollowersAndFollowings()
                }

            } else {
                List {
                    if let userId = followViewModel.userId {
                        Section(header: Text("Подписки")) {
                            ForEach(followViewModel.followings, id: \.id) { following in

                                UserRow(follower: following, userId: userId)
                            }
                        }

                    } else {
                        Section(header: Text("Подписки")) {
                            Text("Загрузка...")

                                .foregroundColor(.gray)
                        }
                    }
                }

                .refreshable {
                    followViewModel.loadInitialFollowersAndFollowings()
                }
            }
        }

        .onAppear {
            followViewModel.loadInitialFollowersAndFollowings()

            followViewModel.loadUserId()
        }
    }
}

struct UserRow: View {
    @ObservedObject var follower: FollowUser
    let userId: Int
    @State private var isProfilePresented = false

    var body: some View {
        HStack {
            AsyncImage(url: URL(string: follower.avatar), content: { image in
                image
                    .resizable()
                    .frame(width: 40, height: 40)
                    .cornerRadius(20)
            }, placeholder: {
                ProgressView()
            })

            VStack(alignment: .leading) {
                Text(follower.username)
                    .onTapGesture {
                        isProfilePresented = true
                    }
            }

            Spacer()

            Button(action: {
                if follower.followedByUser  {
                    FollowController.shared.unfollowUser (followerId: userId, followingId: follower.id) { result in
                        switch result {
                        case .success:
                            DispatchQueue.main.async {
                                follower.followedByUser  = false
                            }
                            print("Successfully unfollowed user")
                        case let .failure(error):
                            print("Error unfollowing user: \(error.localizedDescription)")
                        }
                    }
                } else {
                    FollowController.shared.followUser (followerId: userId, followingId: follower.id) { result in
                        switch result {
                        case .success:
                            DispatchQueue.main.async {
                                follower.followedByUser  = true
                            }
                            print("Successfully followed user")
                        case let .failure(error):
                            print("Error following user: \(error.localizedDescription)")
                        }
                    }
                }
            }) {
                // Используем иконки из Assets
                Image(follower.followedByUser  ? "UnfollowIcon" : "FollowIcon")
                    .resizable()
                    .frame(width: 85, height: 30)
                    .foregroundColor(follower.followedByUser  ? .red : .green) 
            }
        }
        .sheet(isPresented: $isProfilePresented) {
            NavigationView {
                ProfileView(userId: follower.id)
            }
        }
    }
}
