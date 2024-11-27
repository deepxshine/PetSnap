//
//  CommentsView.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 23.11.2024.
//

import SwiftUI

struct CommentsView: View {
    @ObservedObject var postViewController: PostViewController
    var postId: Int
    @State private var newCommentText: String = ""

    var body: some View {
        VStack {
            List {
                // Проверяем, есть ли комментарии
                if postViewController.comments.isEmpty {
                    Text("Комментариев нет.")
                        .foregroundColor(.gray)
                        .padding()
                } else {
                    ForEach(postViewController.comments) { comment in
                        HStack {
                            Text(comment.comment)
                                .padding()
                            Spacer()
                        }
                        .swipeActions {
                            Button(role: .destructive) {
                                deleteComment(commentId: comment.id)
                            } label: {
                                Label("Удалить", systemImage: "trash")
                            }
                        }
                    }
                }
            }
            
            HStack {
                TextField("Введите комментарий", text: $newCommentText)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                
                Button(action: {
                    addComment()
                }) {
                    Text("Отправить")
                }
            }
            .padding()
        }
        .onAppear {
            // Получаем комментарии при появлении
            postViewController.getComments(postId: postId, page: 0)
        }
    }

    private func addComment() {
        guard !newCommentText.isEmpty else { return }

        postViewController.addComment(postId: postId, commentText: newCommentText)
        newCommentText = "" // Очистить текстовое поле после отправки
    }

    private func deleteComment(commentId: Int) {
        postViewController.removeComment(commentId: commentId, postId: postId)
    }
}
