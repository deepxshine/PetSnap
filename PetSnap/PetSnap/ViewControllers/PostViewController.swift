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
    
    func getPosts(page: Int, completion: @escaping ([Post]?) -> Void){
        if let userId = self.userId {
            PostController.shared.getPosts(userId: userId, page: page) {result in
                switch result {
                case .success(let posts):
                    completion(posts)
                    
                case .failure(_):
                    completion(nil)
                }
            }
        }
    }
    
    func likePost(postId: Int, completion: @escaping (Bool?) -> Void){
            PostController.shared.likePost(postId: postId){result in
                switch result {
                case .success(let result):
                    completion(result)
                    
                case .failure(_):
                    completion(nil)
                }
                
            }
        
    }
}
