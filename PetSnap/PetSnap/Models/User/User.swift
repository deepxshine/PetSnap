//
//  User.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 08.11.2024.
//

import Foundation

struct User: Codable, Identifiable {
    let id: Int
    let username: String
    let avatar: String
}
