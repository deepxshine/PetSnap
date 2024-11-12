//
//  PostService.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 08.11.2024.
//

import Foundation


class PostController{
    static let shared = PostController()
    static let userId: String = String(KeychainManager.shared.getCredentials().userId!)
    private let postUrl = Constants.apiUrl + "/posts/"
    
    private init(){}
    
    func getPosts(userId: Int, page: Int, completion: @escaping(Result<[Post], Error>) -> Void) {
        print("Fetching posts for user ID: \(userId), page: \(page)")
        
        guard let url = URL(string: self.postUrl + "main/\(userId)?page=\(page)&size=\(Constants.postCount)") else {
                    print("Invalid URL")
                    completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
                    return
                }
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        print("Request URL: \(request.url?.absoluteString ?? "No URL")")
        
        let task = URLSession.shared.dataTask(with: request){data, response, error in
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
    
}
