//
//  MainView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct MainView: View {
    @State private var selectedTab = 0
    
    var body: some View {
        TabView(selection: $selectedTab) {
            HomeView()
                .tabItem {
                    Image(systemName: "house")
                    Text("Главная")
                }
                .tag(0)
            
            SearchView()
                .tabItem {
                    Image(systemName: "magnifyingglass")
                    Text("Поиск")
                }
                .tag(2)
            
            AddPostView()
                .tabItem {
                    Image(systemName: "plus.square")
                    Text("Добавить пост")
                }
                .tag(1)
            
            
            
            ProfileView()
                .tabItem {
                    Image(systemName: "person")
                    Text("Профиль")
                }
                .tag(3)
        }
    }
}


#Preview {
    MainView()
}
