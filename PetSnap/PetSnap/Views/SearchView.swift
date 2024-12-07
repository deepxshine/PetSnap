//
//  SearchView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct SearchView: View {
    @StateObject private var searchViewController = SearchViewController()
    @State private var users: [User] = []
    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            VStack {
                TextField("Введите имя пользователя...", text: $searchViewController.username)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .textInputAutocapitalization(.never)
                    .padding()
                    .onChange(of: searchViewController.username) { _, _ in
                        performSearch()
                    }

                List(users, id: \.id) { user in
                    NavigationLink(value: user.id) {
                        HStack {
                            AsyncImage(url: URL(string: user.avatar)) { image in
                                image
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 50, height: 50)
                                    .cornerRadius(25)
                            } placeholder: {
                                ProgressView()
                            }

                            Text(user.username)
                                .font(.headline)
                        }
                    }
                }
            }
            .onReceive(searchViewController.$username) { newValue in
                if newValue.isEmpty {
                    users = []
                }
            }
            .navigationDestination(for: Int.self) { userId in
                ProfileView(userId: userId)
            }
        }
    }

    private func performSearch() {
        guard !searchViewController.username.isEmpty else {
            users = []
            return
        }

        searchViewController.search { fetchedUsers in
            DispatchQueue.main.async {
                if let fetchedUsers = fetchedUsers {
                    self.users = fetchedUsers
                } else {
                    self.users = []
                }
            }
        }
    }
}
