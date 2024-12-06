//
//  PostActionButtonsView.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//
import SwiftUI

struct PostActionButtonsView: View {
    let post: Post
    let onLikeAction: (Post) -> Void
    let onCommentAction: (Int) -> Void

    var body: some View {
        HStack {
            Button(action: { onLikeAction(post) }) {
                HStack {
                    Image(post.likedByUser ? "LikeActive" : "LikeInactive")
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 30, height: 30)
                    Text("\(post.likesCount)")
                }
            }
            .buttonStyle(BorderlessButtonStyle())

            Spacer()

            Button(action: { onCommentAction(post.id) }) {
                HStack {
                    Image("Comment")
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 30, height: 30)
                    Text("\(post.commentsCount)")
                }
            }
            .buttonStyle(BorderlessButtonStyle())
        }
        .padding()
        .padding(.horizontal, 10)
    }
}
