//
//  UserPost.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//

import Combine
import Foundation
import SwiftUI

struct UserPost: Identifiable, Codable {
    let id: Int
    let image: String
    let text: String
    let postTime: String
    let commentsCount: Int
    let likesCount: Int
    let likedByUser: Bool
}

