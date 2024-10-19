//
//  AppViewModel.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import Foundation
import SwiftUI


class AppViewModel: ObservableObject {
    @Published var isAuthenticated = false
    
    func checkAuthStatus(){
        if let _ = KeychainManager.shared.getToken(){
            isAuthenticated = true
        } else {
            isAuthenticated = false
        }
    }
}
