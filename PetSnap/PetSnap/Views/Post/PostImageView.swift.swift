//
//  PostImageView.swift.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//

import SwiftUI

struct PostImageView: View {
    let imageUrl: String

    var body: some View {
        VStack(alignment: .leading) {
            if let url = URL(string: imageUrl) {
                AsyncImage(url: url) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(maxWidth: .infinity)
                } placeholder: {
                    ProgressView()
                }
            }
        }
        .padding(.bottom, -10)
    }
}
