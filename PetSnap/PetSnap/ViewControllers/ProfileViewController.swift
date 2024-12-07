//
//  ProfileViewController.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//
import Foundation
import SwiftUI

class ProfileViewModel: ObservableObject {

    @Published var profileResponse: UserProfileResponse?

    @Published var isLoading = false

    @Published var error: Error?

    

    private let userId: Int

    private let profileController: ProfileController

    

    init(userId: Int, profileController: ProfileController = .shared) {

        self.userId = userId

        self.profileController = profileController

    }

    

    func loadProfile() {

        isLoading = true

        

        profileController.fetchUserProfile(userId: userId) { [weak self] result in

            DispatchQueue.main.async {

                self?.isLoading = false

                

                switch result {

                case .success(let profileResponse):

                    self?.profileResponse = profileResponse

                case .failure(let error):

                    self?.error = error

                    print("Error loading profile: \(error)")

                }

            }

        }

    }

}
