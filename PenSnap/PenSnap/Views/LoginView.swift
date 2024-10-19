//
//  LoginView.swift
//  petsnap
//
//  Created by Алексей Евдокимов on 18.10.2024.
//

// Views/LoginView.swift
import SwiftUI

struct LoginView: View {
    @StateObject private var authViewModel = AuthViewModel()
    
    var body: some View {
        VStack {
            
            TextField("Username", text: $authViewModel.username)
                .padding()
                .background(Color.gray.opacity(0.2))
                .cornerRadius(5.0)
            
            SecureField("Password", text: $authViewModel.password)
                .padding()
                .background(Color.gray.opacity(0.2))
                .cornerRadius(5.0)
            
            if let errorMessage = authViewModel.errorMessage {
                Text(errorMessage)
                    .foregroundColor(.red)
                    .padding()
            }
            
            Button("Login") {
                print("pop")
                authViewModel.login { success in
                    if success {
                        print("l")
                        // Действие при успешном логине (например, переход к основному экрану)
                    }
                }
            }
            .padding()
            .background(Color.blue)
            .foregroundColor(.white)
            .cornerRadius(10)
        }
        .padding()
    }
}

#Preview {
    LoginView()
}
