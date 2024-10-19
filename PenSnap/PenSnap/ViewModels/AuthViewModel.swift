//
//  AuthViewModel.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//


import Foundation

class AuthViewModel: ObservableObject {
    @Published var username: String = ""
    @Published var password: String = ""
    @Published var errorMessage: String? = ""
    
    func login(completion: @escaping (Bool) -> Void) {
        AuthService.shared.login(username: username, password: password) {result in
            switch result {
            case .success(let token):
                KeychainManager.shared.saveToken(token)
                DispatchQueue.main.async {
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
}
