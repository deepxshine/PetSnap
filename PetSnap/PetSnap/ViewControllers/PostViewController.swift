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
    @Published var comments: [Comment] = []
    @Published var errorMessage: String?
    @Published var posts: [Post] = [] // Массив постов для обновления
    private var userId = KeychainManager.shared.getCredentials().userId

    init() {
        appViewModel.checkAuthStatus()
    }

    func getPosts(page: Int, completion: @escaping ([Post]?) -> Void) {
            if let userId = self.userId {
                PostController.shared.getPosts(userId: userId, page: page) { result in
                    DispatchQueue.main.async {
                        switch result {
                        case .success(let posts):
                            // Добавляем новые посты или заменяем существующие
                            if page == 0 {
                                self.posts = posts
                            } else {
                                self.posts.append(contentsOf: posts)
                            }
                            completion(posts)
                            
                        case .failure(_):
                            completion(nil)
                        }
                    }
                }
            }
        }
    
    func createPost(image: UIImage, text: String, completion: @escaping (Post?) -> Void) {
        guard let userId = self.userId else {
            errorMessage = "User ID is not available."
            completion(nil)
            return
        }

           PostController.shared.createPost(userId: userId, image: image, text: text) { result in
               DispatchQueue.main.async {
                   switch result {
                   case .success(let newPost):
                       self.posts.insert(newPost, at: 0)
                       self.errorMessage = nil
                       print("Post created successfully: \(newPost.id)")
                       completion(newPost)
                
                   case .failure(let error):
                       self.errorMessage = error.localizedDescription
                       print("Error creating post: \(error.localizedDescription)")
                       completion(nil)
                   }
               }
           }
       }


    func likePost(postId: Int, completion: @escaping (Bool?) -> Void) {
            PostController.shared.likePost(postId: postId) { result in
                DispatchQueue.main.async {
                    switch result {
                    case .success(let result):
                        // Обновляем состояние лайка в локальном массиве постов
                        if let index = self.posts.firstIndex(where: { $0.id == postId }) {
                            self.posts[index].likedByUser.toggle()
                            self.posts[index].likesCount += self.posts[index].likedByUser ? 1 : -1
                        }
                        completion(result)
                    
                    case .failure(_):
                        completion(nil)
                    }
                }
            }
        }

    func getComments(postId: Int, page: Int) {
        guard let userId = self.userId else {
            errorMessage = "User  ID is not available."
            return
        }
        
        CommentsController.shared.getComments(postId: postId, userId: userId, page: page) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let comments):
                    self.comments = comments
                    self.errorMessage = nil
                    
                case .failure(let error):
                    self.errorMessage = error.localizedDescription
                    print("Error fetching comments: \(error.localizedDescription)")
                }
            }
        }
    }
    
    func addComment(postId: Int, commentText: String) {
        guard let userId = self.userId else {
            errorMessage = "User  ID is not available."
            return
        }
        
        CommentsController.shared.addComment(postId: postId, userId: userId, commentText: commentText) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let commentResponse):
                    self.comments.append(commentResponse.comment) // Добавляем новый комментарий в список
                    self.updateCommentsCount(for: postId, increment: true) // Обновляем количество комментариев
                    self.errorMessage = nil
                    print("Comment added: \(commentResponse.comment.comment)")
                    
                case .failure(let error):
                    self.errorMessage = error.localizedDescription
                    print("Error adding comment: \(error.localizedDescription)")
                }
            }
        }
    }
    
    func removeComment(commentId: Int, postId: Int) {
        guard let userId = self.userId else {
            errorMessage = "User  ID is not available."
            return
        }

        CommentsController.shared.removeComment(commentId: commentId, userId: userId) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let commentResponse):
                    self.comments.removeAll { $0.id == commentResponse.comment.id } // Удаляем комментарий из списка
                    self.updateCommentsCount(for: postId, increment: false) // Обновляем количество комментариев
                    self.errorMessage = nil
                    print("Comment removed: \(commentResponse.comment.comment)")

                case .failure(let error):
                    self.errorMessage = error.localizedDescription
                    print("Error removing comment: \(error.localizedDescription)")
                }
            }
        }
    }
    
    func updateCommentsCount(for postId: Int, increment: Bool) {
        DispatchQueue.main.async {
            if let index = self.posts.firstIndex(where: { $0.id == postId }) {
                if increment {
                    self.posts[index].commentsCount += 1
                } else {
                    self.posts[index].commentsCount -= 1
                }
            }
        }
    }
}

extension PostViewController {
    func loadInitialPosts() {
        getPosts(page: 0) { _ in }
    }
}
