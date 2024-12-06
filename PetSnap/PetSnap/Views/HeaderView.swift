//
//  HeaderView.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 06.12.2024.
//
import SwiftUI

struct HeaderView: View {
    var body: some View {
        HStack {
            Image("PetSnapText")
                .resizable()
                .frame(width: 170, height: 35)
            Spacer(minLength: 1)
            Image("Logo")
                .resizable()
                .frame(width: 35, height: 35)
        }
        .padding()
        .padding(.bottom, -15)
        .background(.clear)
        .edgesIgnoringSafeArea(.all)
    }
}
