//
//  PostController.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 08.11.2024.
//

import Foundation
import SwiftUI

class PostController {
    static let shared = PostController()
    private let userId: String = .init(KeychainManager.shared.getCredentials().userId!)
    private let postUrl = ConstantsService.apiUrl + "/posts/main/"
    private let likeUrl = ConstantsService.apiUrl + "/posts/like/"
    private let postPostUrl = ConstantsService.apiUrl + "/posts/createPost/"

    private init() {}

    func getPosts(userId: Int, page: Int, completion: @escaping (Result<[Post], Error>) -> Void) {
        print("Fetching posts for user ID: \(userId), page: \(page)")

        guard let url = URL(string: postUrl + "\(userId)?page=\(page)&size=\(ConstantsService.postCount)") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        print("Request URL: \(request.url?.absoluteString ?? "No URL")")

        let task = URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request getPosts: \(error.localizedDescription)")
                return
            }

            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }
            do {
                let postResponse = try JSONDecoder().decode(GetPostResponse.self, from: data)
                completion(.success(postResponse.content))
                print("Successfully decoded posts. Count: \(postResponse.content.count)")
            } catch {
                print("Decoding error: \(error.localizedDescription)")
            }
        }
        task.resume()
        print("Request to fetch posts sent.")
    }

    func likePost(postId: Int, completion: @escaping (Result<Bool, Error>) -> Void) {
        guard let url = URL(string: likeUrl + "\(postId)/\(userId)") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }
        var request = URLRequest(url: url)
        request.httpMethod = "PUT"
        print("Request URL: \(request.url?.absoluteString ?? "No URL")")

        let task = URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request getPosts: \(error.localizedDescription)")
                return
            }

            guard let _ = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }

            completion(.success(true))
        }

        task.resume()
        print("Request to fetch posts sent.")
    }
    
    
    func createPost(userId: Int, image: UIImage, text: String, completion: @escaping (Result<Post, Error>) -> Void) {
        guard let encodedText = text.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed),
              let url = URL(string: postPostUrl + "\(userId)?text=\(encodedText)") else {
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }

            let boundary = UUID().uuidString
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")

            var data = Data()


            data.append("--\(boundary)\r\n".data(using: .utf8)!)
            data.append("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"\r\n".data(using: .utf8)!)
            data.append("Content-Type: image/jpeg\r\n\r\n".data(using: .utf8)!)
            data.append(image.jpegData(compressionQuality: 0.8)!)
            data.append("\r\n--\(boundary)--\r\n".data(using: .utf8)!)

            let task = URLSession.shared.uploadTask(with: request, from: data) { responseData, response, error in

                if let error = error {
                    completion(.failure(error))
                    return
                }

                guard let data = responseData else {
                    completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                    return
                }

                do {
                    let post = try JSONDecoder().decode(Post.self, from: data)
                    completion(.success(post))
                } catch {
                    completion(.failure(error))
                }
            }

            task.resume()

        }

}

