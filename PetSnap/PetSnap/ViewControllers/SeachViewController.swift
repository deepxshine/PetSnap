//
//  SeachViewController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 13.11.2024.
//

import Foundation
import SwiftUI

class SeachViewController: ObservableObject {
    @Published var appViewModel = AppViewController()
    @Published var username: String = ""
    @Published var errorMessage: String? = nil

    init() {
        appViewModel.checkAuthStatus()
    }

    func search(completion: @escaping ([User]?) -> Void) {
        if username != "" {
            SearchController.shared.search(username: username) { result in
                switch result {
                case let .success(user):
                    completion(user)

                case let .failure(error):
                    print(error)
                    completion(nil)
                }
            }
        }
    }
}
