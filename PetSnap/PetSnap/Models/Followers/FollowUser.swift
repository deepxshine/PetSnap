//
//  FollowUser.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 04.12.2024.
//

import Foundation
import Combine

class FollowUser: Codable, Identifiable, ObservableObject {
    @Published var id: Int
    let username: String
    let avatar: String
    @Published var followedByUser: Bool

    init(id: Int, username: String, avatar: String, followedByUser: Bool) {
        self.id = id
        self.username = username
        self.avatar = avatar
        self.followedByUser = followedByUser
    }

    enum CodingKeys: String, CodingKey {
        case id
        case username
        case avatar
        case followedByUser
    }

    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(Int.self, forKey: .id)
        username = try container.decode(String.self, forKey: .username)
        avatar = try container.decode(String.self, forKey: .avatar)
        followedByUser = try container.decode(Bool.self, forKey: .followedByUser)
    }

    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(username, forKey: .username)
        try container.encode(avatar, forKey: .avatar)
        try container.encode(followedByUser, forKey: .followedByUser)
    }
}
