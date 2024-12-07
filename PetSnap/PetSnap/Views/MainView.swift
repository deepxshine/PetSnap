//
//  MainView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct MainView: View {
    @State private var selectedTab = 0

    init() {
        UITabBar.appearance().backgroundColor = UIColor(red: 232 / 255, green: 226 / 255, blue: 214 / 255, alpha: 1)

        UITabBar.appearance().barTintColor = UIColor(red: 232 / 255, green: 226 / 255, blue: 214 / 255, alpha: 1)
    }

    var body: some View {
        ZStack {
            Image("Bg")

                .resizable()
                .aspectRatio(contentMode: .fill)
                .edgesIgnoringSafeArea(.all)

            VStack {
                HeaderView()
                    .background(Color.white.opacity(0))
                    .padding(.top, 49)

                TabView(selection: $selectedTab) {
                    HomeView()
                        .background(Color.white.opacity(0))
                        .tabItem {
                            Image(selectedTab == 0 ? "HomeActive" : "HomeInactive")

                                .renderingMode(.original)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        }

                        .tag(0)

                    SearchView()

                        .background(Color.white.opacity(0))
                        .tabItem {
                            Image(selectedTab == 2 ? "SearchActive" : "SearchInactive")
                                .renderingMode(.original)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        }

                        .tag(2)

                    AddPostView()
                        .background(Color.white.opacity(0))
                        .tabItem {
                            Image(selectedTab == 5 ? "AddPostActive" : "AddPostInactive")

                                .renderingMode(.original)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        }

                        .tag(5)

                    FollowView()

                        .background(Color.white.opacity(0))
                        .tabItem {
                            Image(selectedTab == 4 ? "SubsActive" : "SubsInactive")

                                .renderingMode(.original)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        }

                        .tag(4)

                    ProfileView(userId: 1)

                        .background(Color.white.opacity(0))
                        .tabItem {
                            Image(selectedTab == 3 ? "ProfileActive" : "ProfileInactive")

                                .renderingMode(.original)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        }

                        .tag(3)
                }

                .accentColor(.black)
            }
        }
    }
}
