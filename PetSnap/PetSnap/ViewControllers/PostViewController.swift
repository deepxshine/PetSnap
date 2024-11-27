//
//  PostViewController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 13.11.2024.
//

import Foundation
import SwiftUI

class PostViewController: ObservableObject {
    @Published var appViewModel = AppViewController()
    private var userId = KeychainManager.shared.getCredentials().userId

    init() {
        appViewModel.checkAuthStatus()
    }

    func getPosts(page: Int, completion: @escaping ([Post]?) -> Void) {
        if let userId = userId {
            PostController.shared.getPosts(userId: userId, page: page) { result in
                switch result {
                case let .success(posts):
                    completion(posts)

                case .failure:
                    completion(nil)
                }
            }
        }
    }

    func likePost(postId: Int, completion: @escaping (Bool?) -> Void) {
        PostController.shared.likePost(postId: postId) { result in
            switch result {
            case let .success(result):
                completion(result)

            case .failure:
                completion(nil)
            }
        }
    }
}
