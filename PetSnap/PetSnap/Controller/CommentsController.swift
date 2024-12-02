//
//  CommentsController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 23.11.2024.
//

import Foundation

class CommentsController {
    static let shared = CommentsController()
    private let commentsUrl = ConstantsService.apiUrl + "/comments/"
    
    private init() {}
    
    // Добавление комментария
    func addComment(postId: Int, userId: Int, commentText: String, completion: @escaping(Result<CommentResponse, Error>) -> Void) {
        guard let url = URL(string: self.commentsUrl + "addComment/\(userId)/\(postId)") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let json: [String: Any] = ["comment": commentText]
        let jsonData = try? JSONSerialization.data(withJSONObject: json)
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request addComment: \(error.localizedDescription)")
                return
            }
            
            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }
            
            do {
                let commentResponse = try JSONDecoder().decode(CommentResponse.self, from: data)
                completion(.success(commentResponse))
                print("Successfully added comment: \(commentResponse.comment.comment)")
            } catch {
                print("Decoding error: \(error.localizedDescription)")
            }
        }
        
        task.resume()
        print("Request to add comment sent.")
    }
    
    // Получение комментариев
    func getComments(postId: Int, userId: Int, page: Int, completion: @escaping(Result<[Comment], Error>) -> Void) {
        guard let url = URL(string: self.commentsUrl + "\(postId)/\(userId)?page=\(page)&size=9") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }
        print(postId)
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        print(url)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request getComments: \(error.localizedDescription)")
                return
            }
            
            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }
            
            do {
                let commentResponse = try JSONDecoder().decode(GetCommentsResponse.self, from: data)
                completion(.success(commentResponse.comments))
                print("Successfully fetched comments. Count: \(commentResponse.comments.count)")
            } catch {
                print("Decoding error: \(error.localizedDescription)")
            }
        }
        
        task.resume()
        print("Request to fetch comments sent.")
    }
    
    // Удаление комментария
    func removeComment(commentId: Int, userId: Int, completion: @escaping(Result<CommentResponse, Error>) -> Void) {
        guard let url = URL(string: self.commentsUrl + "removeComment/\(userId)/\(commentId)") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"
        
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request removeComment: \(error.localizedDescription)")
                return
            }
            
            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }
            
            do {
                let commentResponse = try JSONDecoder().decode(CommentResponse.self, from: data)
                completion(.success(commentResponse))
                print("Successfully removed comment: \(commentResponse.comment.comment)")
            } catch {
                print("Decoding error: \(error.localizedDescription)")
            }
        }
        
        task.resume()
        print(" Request to remove comment sent.")
    }
}

