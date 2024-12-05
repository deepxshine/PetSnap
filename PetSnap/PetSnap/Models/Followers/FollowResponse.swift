//
//  FollowResponse.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 04.12.2024.
//

import Foundation

struct FollowResponse: Codable {
    let followersList: [FollowUser]
    let followingsList: [FollowUser]

}
