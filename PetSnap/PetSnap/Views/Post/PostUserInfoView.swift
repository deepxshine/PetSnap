//
//  PostUserInfoView.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//

import SwiftUI

struct PostUserInfoView: View {
    let post: Post

    var body: some View {
        VStack(alignment: .leading) {
            Text(post.user.username)
                .font(.headline)
                .foregroundColor(.black)

            if let text = post.text {
                Text(text)
                    .font(.callout)
            }

            Text(DateFormatterService.shared.formatPostDate(post.postTime))
                .font(.caption)
                .foregroundColor(.gray)
        }
        .padding(.vertical)
        .padding(.leading, 10)
    }
}
