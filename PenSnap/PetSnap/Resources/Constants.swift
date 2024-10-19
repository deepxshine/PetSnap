//
//  Constants.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import Foundation

class Constants {
    static let apiUrl: String = {
        return ProcessInfo.processInfo.environment["API_URL"] ?? "http://localhost:8000"
    }()
    
}
