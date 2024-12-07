//
//  UserProfileResponse.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//

struct UserProfileResponse: Codable {
    let id: Int
    let username: String
    let avatar: String?
    let bio: String?
    let posts: [UserPost]
}
