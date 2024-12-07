//
//  AuthView.swift
//  petsnap
//
//  Created by Алексей Евдокимов on 18.10.2024.
//

import SwiftUI

struct AuthView: View {
    @EnvironmentObject var appViewModel: AppViewController

    var body: some View {
        ZStack {
            Image("Bg")
                .resizable()
                .scaledToFill()
                .edgesIgnoringSafeArea(.all)

            VStack {
                HeaderView()
                    .background(Color.white.opacity(0))
                    .padding(.top, 49)

                Spacer(minLength: 20)

                RoundedRectangle(cornerRadius: 20)
                    .fill(Color(red: 238 / 255, green: 238 / 255, blue: 238 / 255))
                    .frame(width: 380, height: 400)
                    .overlay(
                        VStack {
                            Text("Welcome To PetSnap")
                                .font(.largeTitle)
                                .fontWeight(.bold)
                                .foregroundColor(.black)
                                .padding(.top, 20)

                            HStack {
                                TextField("Username", text: $appViewModel.username)
                                    .padding()
                                    .background(Color.gray.opacity(0.2))
                                    .cornerRadius(5.0)
                                    .textInputAutocapitalization(.never)
                            }
                            .padding(.bottom, 10)

                            HStack {
                                SecureField("Password", text: $appViewModel.password)
                                    .padding()
                                    .background(Color.gray.opacity(0.2))
                                    .cornerRadius(5.0)
                            }
                            .padding(.bottom, 10)

                            if let errorMessage = appViewModel.errorMessage {
                                Text(errorMessage)
                                    .foregroundColor(.red)
                                    .padding()
                            }
                            HStack {
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
                                    Image("Login")
                                        .resizable()
                                        .frame(width: 140, height: 55)
                                        .padding()
                                }

                                Button(action: {
                                    // Действие для регистрации
                                }) {
                                    Image("Register")
                                        .resizable()
                                        .frame(width: 170, height: 55)
                                        .padding()
                                }
                            }
                        }
                        .padding()
                    )

                Spacer(minLength: 300)
            }
        }
    }
}

#Preview {
    AuthView()
}
