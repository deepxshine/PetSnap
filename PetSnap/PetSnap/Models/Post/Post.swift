//
//  Post.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 08.11.2024.
//

struct Post: Codable, Identifiable {
    let id: Int
    let image: String
    let text: String?
    let user: User
    let postTime: String
    let commentsCount: Int
    var likesCount: Int
    var likedByUser: Bool
}
