//
//  ProfileController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//
import Foundation

class ProfileController {
    static let shared = ProfileController()

    private init() {}

    func fetchUserProfile(userId: Int, completion: @escaping (Result<UserProfileResponse, Error>) -> Void) {
        guard let url = URL(string: "\(ConstantsService.apiUrl)/user/userPage/\(userId)?page=0&size=9") else {
            completion(.failure(ProfileError.invalidURL))
            return
        }

        URLSession.shared.dataTask(with: url) { data, _, error in
            if let error = error {
                completion(.failure(error))
                return
            }

            guard let data = data else {
                completion(.failure(ProfileError.noData))
                return
            }

            do {
                let decoder = JSONDecoder()
                let profileData = try decoder.decode(UserProfileResponse.self, from: data)
                completion(.success(profileData))
            } catch {
                completion(.failure(error))
            }

        }.resume()
    }

    // Остальные методы остаются без изменений

    enum ProfileError: Error {
        case invalidURL
        case noData
        case imageLoadingFailed
        case updateFailed
    }
}
