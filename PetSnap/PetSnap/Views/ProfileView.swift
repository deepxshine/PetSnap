//
//  ProfileView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var appViewModel: AppViewModel
    
    var body: some View {
        VStack {
            Text("Профиль")
                .font(.largeTitle)
                .padding()
            
            Button(action: {
                appViewModel.logout() // Выход из системы
            }) {
                Text("Выход")
                    .font(.headline)
                    .padding()
                    .background(Color.red)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .padding()
        }
    }
}

#Preview {
    ProfileView()
        .environmentObject(AppViewModel())
}
