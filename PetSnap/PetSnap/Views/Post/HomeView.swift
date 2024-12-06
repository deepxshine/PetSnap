//
//  HomeView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import SwiftUI

struct HomeView: View {
    @StateObject private var postViewController = PostViewController()
    @State private var posts: [Post] = []
    @State private var isLoading = false
    @State private var isFinished = false
    @State private var currentPage = 0
    @State private var selectedPostId: Int?
    @State private var showCommentsView = false

    var body: some View {
        NavigationStack {
            List {
                ForEach(posts) { post in
                    PostContentView(
                        post: post,
                        onLikeAction: toggleLike(for:),
                        onCommentAction: { postId in
                            selectedPostId = postId
                            showCommentsView.toggle()
                        }
                    )
                  
                    .listRowBackground(Color.clear)
                    Spacer()
                }
                

                if !isFinished && isLoading {
                    ProgressView("Загрузка дополнительных постов...")

                        .frame(maxWidth: .infinity, alignment: .center)
                        .padding()
                        .listRowBackground(Color.clear)
                }

                if !isFinished && !isLoading {
                    Color.clear
                        .onAppear {
                            loadMorePosts()
                        }

                        .listRowBackground(Color.clear)
                }
            }


            .background(Color.clear) // Прозрачный общий фон
            .listStyle(PlainListStyle())
            .sheet(isPresented: $showCommentsView) {
                if let postId = selectedPostId {
                    CommentsView(postViewController: postViewController, postId: postId)
                }
            }

            .refreshable {
                refreshPosts()
            }
        }

        .background(Color.clear)
        .padding(0)
    }

    private func updateCommentsCount(for postId: Int, newCount: Int) {
        if let index = posts.firstIndex(where: { $0.id == postId }) {
            posts[index].commentsCount = newCount
        }
    }

    private func loadMorePosts() {
        guard !isLoading else { return }

        isLoading = true
        loadPosts(page: currentPage)
    }

    private func loadPosts(page: Int) {
        postViewController.getPosts(page: page) { fetchedPosts in
            DispatchQueue.main.async {
                if let fetchedPosts = fetchedPosts, !fetchedPosts.isEmpty {
                    self.posts.append(contentsOf: fetchedPosts)
                    self.currentPage += 1

                    if fetchedPosts.count < ConstantsService.postCount {
                        isFinished = true
                    }
                } else {
                    isFinished = true
                }
                isLoading = false
            }
        }
    }

    private func refreshPosts() {
        currentPage = 0
        posts.removeAll()
        loadPosts(page: currentPage)
    }

    private func toggleLike(for post: Post) {
        postViewController.likePost(postId: post.id) { success in
            if success == true {
                if let index = posts.firstIndex(where: { $0.id == post.id }) {
                    posts[index].likedByUser.toggle()
                    posts[index].likesCount += posts[index].likedByUser ? 1 : -1
                }
            } else {
                print("Ошибка при попытке поставить лайк на пост с ID \(post.id)")
            }
        }
    }
}

#Preview {
    HomeView()
}
