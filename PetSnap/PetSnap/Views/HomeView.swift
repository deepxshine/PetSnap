//
//  HomeView.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//
import SwiftUI

struct HomeView: View {
    @StateObject private var postViewModel = PostViewModel()
    @State private var posts: [Post] = []
    @State private var isLoading = false
    @State private var isFinished = false
    @State private var currentPage = 0

    var body: some View {
        NavigationStack {
            List {
                ForEach(posts) { post in
                    VStack(alignment: .leading){
                        VStack(alignment: .leading) {
                            if let imageUrl = URL(string: post.image) {
                                AsyncImage(url: imageUrl) { image in
                                    image
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(maxWidth: .infinity)
                                } placeholder: {
                                    ProgressView()
                                }
                            }
                        }.padding(.bottom, -10)
                        
                        VStack(alignment: .leading){
                            
                            Text("\(post.user.username)")
                                .font(.headline)
                                .foregroundColor(.black)
                            
                            if let text = post.text {
                                Text(text)
                                    .font(.callout)
                                
                            }
                            
                            Text("\(post.postTime)")
                            Text("Количество лайков \(post.likesCount)")
                                .font(Font.system(size:10))
                            Text("Количество комментариев \(post.commentsCount)")
                                .font(Font.system(size:10))
                            
                            
                            
                        }
                        .padding(.vertical)
                        .padding(.leading, 10)
                        .padding(.bottom, 20)
                    }
                    .listRowInsets(EdgeInsets())
                    .padding(.bottom, 15)
                }

                if !isFinished && isLoading {
                    ProgressView("Загрузка дополнительных постов...")
                        .frame(maxWidth: .infinity, alignment: .center)
                        .padding()
                }

                if !isFinished && !isLoading {
                    Color.clear.onAppear {
                        loadMorePosts()
                    }
                }
            }
            .navigationTitle("")
            .listStyle(PlainListStyle())
        }
        .padding(0)
        .onAppear(perform: loadInitialPosts)
    }

    private func loadInitialPosts() {
        loadPosts(page: currentPage)
    }

    private func loadMorePosts() {
        guard !isLoading else { return }
        
        isLoading = true
        loadPosts(page: currentPage)
    }

    private func loadPosts(page: Int) {
        postViewModel.getPosts(page: page) { fetchedPosts in
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
}

#Preview {
    HomeView()
}
