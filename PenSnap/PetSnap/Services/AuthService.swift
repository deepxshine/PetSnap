//
//  AuthService.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import Foundation

class AuthService{
    static let shared = AuthService()
    private let loginURL = URL(string: Constants.apiUrl + "/user/login")!
    
    private init() {}
    
    func login(username: String, password: String, completion: @escaping (Result<String, Error>)-> Void) {
        var request = URLRequest(url: loginURL)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let loginReqest = LoginRequest(username: username, password: password)
        request.httpBody = try? JSONEncoder().encode(loginReqest)
        let task = URLSession.shared.dataTask(with: request) {data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            
            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                return
            }
            do {
                let loginResponse = try JSONDecoder().decode(LoginResponse.self, from: data)
                completion(.success(loginResponse.token))
                
            } catch {
                completion(.failure(error))
            }
        }
        task.resume()
    }
}
