//
//  SearchView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct SearchView: View {
    @StateObject private var searchViewController = SeachViewController()
    @State private var users: [User] = []

    var body: some View {
        VStack {
            TextField("Введите имя пользователя...", text: $searchViewController.username)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .textInputAutocapitalization(.never)
                .padding()
                .onChange(of: searchViewController.username) { _, _ in
                    performSearch()
                }

            List(users) { user in
                HStack {
                    AsyncImage(url: URL(string: user.avatar)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 50, height: 50)
                    } placeholder: {
                        ProgressView()
                    }

                    Text(user.username)
                        .font(.headline)
                }
            }
        }
        .navigationTitle("Поиск пользователей")
        .onReceive(searchViewController.$username) { newValue in
            if newValue.isEmpty {
                users = []
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

#Preview {
    NavigationStack {
        SearchView()
    }
}
