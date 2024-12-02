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
    var commentsCount: Int // Изменено на var для возможности изменения
    var likesCount: Int
    var likedByUser:  Bool
}
