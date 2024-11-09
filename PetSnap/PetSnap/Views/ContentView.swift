//
//  ContentView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct ContentView: View {
    @EnvironmentObject var appViewModel: AppViewModel
    var body: some View {
        if appViewModel.isAuthenticated {
            MainView()
                .environmentObject(appViewModel)
        } else {
            AuthView()
                .environmentObject(appViewModel)
        }
    }
}
