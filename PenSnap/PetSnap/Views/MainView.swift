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
                }
                .tag(0)
            
            SearchView()
                .tabItem {
                    Image(systemName: "magnifyingglass")
                }
                .tag(2)
            
            AddPostView()
                .tabItem {
                    Image(systemName: "plus.square")
                }
                .tag(1)
            
            
            
            ProfileView()
                .tabItem {
                    Image(systemName: "person")
                }
                .tag(3)
        }
    }
}


#Preview {
    MainView()
}
