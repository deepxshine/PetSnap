import SwiftUI

struct CommentsView: View {
    @ObservedObject var postViewController: PostViewController
    var postId: Int
    @State private var newCommentText: String = ""

    var body: some View {
        VStack {
            List {
                if postViewController.comments.isEmpty {
                    Text("Комментариев нет.")
                        .foregroundColor(.gray)
                        .padding()
                } else {
                    ForEach(postViewController.comments) { comment in
                        VStack(alignment: .leading) {
                            HStack {
                                Text(comment.user.username)
                                    .font(.headline)
                                Spacer()
                                Text(comment.commentTime)
                                    .font(.subheadline)
                                    .foregroundColor(.gray)
                            }

                            if let parsedComment = parseComment(from: comment.comment) {
                                Text(parsedComment)
                                    .padding(.top, 2)
                                    .padding(.bottom, 5)
                            } else {
                                Text("Ошибка при распарсивании комментария")
                                    .foregroundColor(.red)
                            }
                        }
                        .padding()
                        .background(comment.commentedByUser ? Color.blue.opacity(0.1) : Color.clear)
                        .cornerRadius(8)
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
            postViewController.getComments(postId: postId, page: 0)
        }
    }

    private func addComment() {
        guard !newCommentText.isEmpty else { return }

        postViewController.addComment(postId: postId, commentText: newCommentText)
        newCommentText = ""
    }

    private func deleteComment(commentId: Int) {
        postViewController.removeComment(commentId: commentId, postId: postId)
    }

    private func parseComment(from jsonString: String) -> String? {
        let jsonData = jsonString.data(using: .utf8)

        guard let data = jsonData else {
            print("Ошибка при преобразовании строки в данные.")
            return nil
        }

        let decoder = JSONDecoder()

        do {
            let commentObject = try decoder.decode(CommentObject.self, from: data)
            return commentObject.comment
        } catch {
            print("Ошибка при декодировании JSON: \(error)")
            return nil
        }
    }
}
