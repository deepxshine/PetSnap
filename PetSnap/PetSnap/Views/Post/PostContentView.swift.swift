//
//  PostContentView.swift.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//

import SwiftUI

struct PostContentView: View {
    let post: Post
    let onLikeAction: (Post) -> Void
    let onCommentAction: (Int) -> Void

    var body: some View {
        VStack(alignment: .leading) {
            PostImageView(imageUrl: post.image)
            PostUserInfoView(post: post)
            PostActionButtonsView(
                post: post,
                onLikeAction: onLikeAction,
                onCommentAction: onCommentAction
            )
        }
        .listRowInsets(EdgeInsets())
        .padding(.bottom, 15)
    }
}
