//
//  FollowController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 04.12.2024.
//

import Foundation

class FollowController {
    static let shared = FollowController()
    private let userId: String = .init(KeychainManager.shared.getCredentials().userId!)
    private let friendshipUrl = ConstantsService.apiUrl + "/user/"
    private let followUrl = ConstantsService.apiUrl + "/user/follow/"
    private let unfollowUrl = ConstantsService.apiUrl + "/user/unfollow/"

    private init() {}

    func getFollowersAndFollowings(userId: Int, completion: @escaping (Result<FollowResponse, Error>) -> Void) {
        guard let url = URL(string: "\(friendshipUrl)\(userId)/friendship") else {
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        print(url)
        let task = URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request getFollowersAndFollowings: \(error.localizedDescription)")
                return
            }

            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }

            do {
                let response = try JSONDecoder().decode(FollowResponse.self, from: data)
                completion(.success(response))
                print("Successfully decoded followers and followings.")
            } catch {
                print("Decoding error: \(error.localizedDescription)")
            }
        }
        task.resume()
        print("Request to fetch followers and followings sent.")
    }

    // Подписаться на пользователя
    func followUser(followerId: Int, followingId: Int, completion: @escaping (Result<FollowUser, Error>) -> Void) {
        guard let url = URL(string: "\(followUrl)\(followerId)/\(followingId)") else {
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        print(url)
        let task = URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request followUser: \(error.localizedDescription)")
                return
            }

            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }

            do {
                let user = try JSONDecoder().decode(FollowUser.self, from: data)
                completion(.success(user))
                print("Successfully followed user.")
            } catch {
                print("Decoding error: \(error.localizedDescription)")
            }
        }
        task.resume()
        print("Request to follow user sent.")
    }

    // Отписаться от пользователя
    func unfollowUser(followerId: Int, followingId: Int, completion: @escaping (Result<FollowUser, Error>) -> Void) {
        guard let url = URL(string: "\(unfollowUrl)\(followerId)/\(followingId)") else {
            completion(.failure(NSError(domain: "Invalid URL", code: -1, userInfo: nil)))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        print(url)
        let task = URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request unfollowUser: \(error.localizedDescription)")
                return
            }

            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }

            do {
                let user = try JSONDecoder().decode(FollowUser.self, from: data)
                completion(.success(user))
                print("Successfully unfollowed user.")
            } catch {
                print("Decoding error: $$error.localizedDescription)")
            }
        }
        task.resume()
        print("Request to unfollow user sent.")
    }
}
