//
//  PostViewModel.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 08.11.2024.
//

import Foundation
import SwiftUI


class PostViewModel: ObservableObject {
    @Published var appViewModel = AppViewModel()
    private var userId = KeychainManager.shared.getCredentials().userId
    
    
    init() {
        appViewModel.checkAuthStatus()
    }
    
    func getPosts(page: Int, completion: @escaping ([Post]?) -> Void){
        if let userId = self.userId {
            PostController.shared.getPosts(userId: userId, page: page) {result in
                print(result)
                switch result {
                case .success(let posts):
                    completion(posts)
                    print("Post has been got")
                    
                case .failure(let error):
                    print(error)
                    completion(nil)
                }
            }
        }
    }
}
