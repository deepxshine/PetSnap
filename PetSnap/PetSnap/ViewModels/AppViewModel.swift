//
//  AppViewModel.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import Foundation
import SwiftUI


class AppViewModel: ObservableObject {
    @Published var isAuthenticated = false
    @Published var username: String = ""
    @Published var password: String = ""
    @Published var errorMessage: String? = nil
    
    init() {
        checkAuthStatus()
    }
    
    func login(completion: @escaping (Bool) -> Void) {
        AuthController.shared.login(username: username, password: password) { result in
            switch result {
            case .success(let credentials):
                let token = credentials.token
                let userId = credentials.userId
                print(userId)
                
                let _ = KeychainManager.shared.saveCredentials(token: token, userId: userId)
                
                DispatchQueue.main.async {
                    self.isAuthenticated = true
                    completion(true)
                }
                
            case .failure(let error):
                DispatchQueue.main.async {
                    self.errorMessage = error.localizedDescription
                    completion(false)
                }
            }
        }
    }
    
    func logout() {
        let _ = KeychainManager.shared.deleteCredentials()
        isAuthenticated = false
    }
    
    func checkAuthStatus() {
        if let token = KeychainManager.shared.getCredentials().token {
            print(token)
            isAuthenticated = true
        } else {
            isAuthenticated = false
        }
    }
}
