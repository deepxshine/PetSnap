//
//  GetPostResponse.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 08.11.2024.
//

import Foundation

struct GetPostResponse: Codable {
    let content: [Post]
    let pageable: Pageable
    let last: Bool
    let totalPages: Int
    let totalElements: Int
    let first: Bool
    let size: Int
    let number: Int
    let sort: Sort
    let numberOfElements: Int
    let empty: Bool
}
