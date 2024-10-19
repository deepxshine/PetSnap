//
//  ContentView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct ContentView: View {
    @StateObject private var viewModel = AppViewModel()
    
    var body: some View {
        Group {
            if viewModel.isAuthenticated {
                MainView()  // Переход к основному экрану приложения
            } else {
                LoginView() // Экран авторизации
            }
        }
        .onAppear {
            viewModel.checkAuthStatus()
        }
        
    }
}

#Preview {
    ContentView()
}
