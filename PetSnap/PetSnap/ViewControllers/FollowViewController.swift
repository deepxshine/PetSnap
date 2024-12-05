//
//  FollowViewController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 04.12.2024.
//

import Foundation
import SwiftUI

class FollowViewController: ObservableObject {
    @Published var followers: [FollowUser] = []
    @Published var followings: [FollowUser] = []
    @Published var errorMessage: String?
    public var userId = KeychainManager.shared.getCredentials().userId

    init() {
        
    }

    func getFollowersAndFollowings(completion: @escaping ([FollowUser]?, [FollowUser]?) -> Void) {
        guard let userId = self.userId else {
            errorMessage = "User ID is not available."
            completion(nil, nil)
            return
        }

        FollowController.shared.getFollowersAndFollowings(userId: userId) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let response):
                    self.followers = response.followersList
                    self.followings = response.followingsList
                    completion(self.followers, self.followings)
                    
                case .failure(let error):
                    self.errorMessage = error.localizedDescription
                    print("Error fetching followers and followings: $$error.localizedDescription)")
                    completion(nil, nil)
                }
            }
        }
    }

    func unfollowUser(followerId: Int, followingId: Int, completion: @escaping (Bool?) -> Void) {
            FollowController.shared.unfollowUser(followerId: followerId, followingId: followingId) { result in
                DispatchQueue.main.async {
                    switch result {
                    case .success(_):
                        // Обновляем статус подписки в локальных массивах
                        if let followerIndex = self.followers.firstIndex(where: { $0.id == followingId }) {
                            self.followers[followerIndex].followedByUser = false
                        }
                        if let followingIndex = self.followings.firstIndex(where: { $0.id == followingId }) {
                            self.followings[followingIndex].followedByUser = false
                        }
                        completion(true)
                        
                    case .failure(_):
                        completion(false)
                    }
                }
            }
        }

        func followUser(followerId: Int, followingId: Int, completion: @escaping (Bool?) -> Void) {
            FollowController.shared.followUser(followerId: followerId, followingId: followingId) { result in
                DispatchQueue.main.async {
                    switch result {
                    case .success(_):
                        // Обновляем статус подписки в локальных массивах
                        if let followerIndex = self.followers.firstIndex(where: { $0.id == followingId }) {
                            self.followers[followerIndex].followedByUser = true
                        }
                        if let followingIndex = self.followings.firstIndex(where: { $0.id == followingId }) {
                            self.followings[followingIndex].followedByUser = true
                        } else {
                            // Если пользователя нет в списке подписок, добавляем его
                            if var user = try? JSONDecoder().decode(FollowUser.self, from: Data()) {
                                user.id = followingId
                                user.followedByUser = true
                                self.followings.append(user)
                            }
                        }
                        completion(true)
                        
                    case .failure(_):
                        completion(false)
                    }
                }
            }
        }

    func loadInitialFollowersAndFollowings() {
        getFollowersAndFollowings { _, _ in }
    }
    
    func loadUserId() {
            if let userId = KeychainManager.shared.getCredentials().userId {
                self.userId = userId
            }
        }
    
    func updateFollowersOrFollowings(user: FollowUser, isFollowed: Bool) {
            DispatchQueue.main.async {
                if let index = self.followers.firstIndex(where: { $0.id == user.id }) {
                    if !isFollowed {
                        self.followers.remove(at: index)
                    }
                } else if let index = self.followings.firstIndex(where: { $0.id == user.id }) {
                    if isFollowed {
                        self.followings.remove(at: index)
                    }
                }

                if isFollowed {
                    if user.followedByUser {
                        self.followings.append(user)
                    } else {
                        self.followers.append(user)
                    }
                } else {
                    if !user.followedByUser {
                        self.followers.append(user)
                    } else {
                        self.followings.append(user)
                    }
                }
            }
        }
}
