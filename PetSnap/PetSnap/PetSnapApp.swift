//
//  PetSnapApp.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

@main
struct PenSnapApp: App {
    @StateObject private var appViewModel = AppViewController()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(appViewModel)
        }
    }
}
