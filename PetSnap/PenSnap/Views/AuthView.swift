//
//  LoginView.swift
//  petsnap
//
//  Created by Алексей Евдокимов on 18.10.2024.
//

// Views/LoginView.swift
import SwiftUI

struct AuthView: View {
    @EnvironmentObject var appViewModel: AppViewModel
    
    var body: some View {
        VStack {
            
            TextField("Username", text: $appViewModel.username)
                .padding()
                .background(Color.gray.opacity(0.2))
                .cornerRadius(5.0)
                .textInputAutocapitalization(.never)
            
            SecureField("Password", text: $appViewModel.password)
                .padding()
                .background(Color.gray.opacity(0.2))
                .cornerRadius(5.0)
            
            if let errorMessage = appViewModel.errorMessage {
                Text(errorMessage)
                    .foregroundColor(.red)
                    .padding()
            }
            Button(action: {
                            appViewModel.login { success in
                                if success {
                                    appViewModel.isAuthenticated = true
                                    appViewModel.checkAuthStatus()
                                    print("Успешный вход")
                                } else {
                                    print("Ошибка входа")
                                }
                            }
                        }) {
                            Text("Войти")
                                .font(.headline)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        }
            
            
        }
        .padding()
    }
}

#Preview {
    AuthView()
}
