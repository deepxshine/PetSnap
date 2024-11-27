//
//  Commnet.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 23.11.2024.
//

struct Comment: Codable, Identifiable {
    let id: Int
    let comment: String
    let commentTime: String
    let user: User
    let commentedByUser: Bool
}
