package my.project.petsnap.service

import my.project.petsnap.dto.PostWithDetailsDTO
import my.project.petsnap.entity.PostDB
import my.project.petsnap.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(private val postRepository: PostRepository) {

    fun createPost(post: PostDB): PostDB {
        return postRepository.save(post)
    }

    fun deletePost(postId: Long) {
        postRepository.deleteById(postId)
    }

    fun getPostWithLikesAndComments(postId: Long): PostWithDetailsDTO {
        val opPost = postRepository.findById(postId)

        if (!opPost.isEmpty) {
            val post = opPost.get()
            return PostWithDetailsDTO(
                post = post,
                likes = post.likes,
                comments = post.comments
            )
        }
        throw RuntimeException("Post not found")
    }

    fun getPostsByUserId(userId: Long): List<PostDB> {
        return postRepository.findByUserId(userId)
    }
}