package my.project.petsnap.service

import my.project.petsnap.dto.PostResponseDTO
import my.project.petsnap.dto.PostsOnMainPageResponseDTO
import my.project.petsnap.dto.UserSearchResponseDTO
import my.project.petsnap.entity.PostDB
import my.project.petsnap.repository.LikeRepository
import my.project.petsnap.repository.PostRepository
import my.project.petsnap.repository.UserRepository
import my.project.petsnap.utils.ImageUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val imageUtils: ImageUtils,
    private val likeRepository: LikeRepository,

    ) {

    fun createPost(file: MultipartFile, text: String?, userId: Long): ResponseEntity<Any> {
        // check if file is an image
        if (!imageUtils.isImageFile(file)) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Uploaded file is not an image"))
        }
        // generate image url
        val imageUrl = imageUtils.generateUrl(file)

        val user = userRepository.findById(userId).orElse(null)

        if (user == null) {
            return ResponseEntity.badRequest().body(mapOf("message" to "User not found"))
        } else {
            val post = PostDB(
                image = imageUrl,
                text = text,
                postTime = LocalDateTime.now(),
                user = user,
                comments = mutableListOf(),
                likes = mutableListOf()
            )

            postRepository.save(post)

            val createdPost = PostResponseDTO(
                id = post.id!!,
                image = post.image,
                text = post.text,
                postTime = post.postTime,
                commentsCount = post.comments.count(),
                likesCount = post.likes.count(),
                likedByUser = likeRepository.existsByUserAndPost(user, post),
            )
            return ResponseEntity.ok(createdPost)
        }
    }

    fun deletePost(postId: Long): ResponseEntity<Any> {
        try {
            val post = postRepository.findById(postId).orElse(null)
            if (post == null) {
                return ResponseEntity.badRequest().body(mapOf("message" to "Post not found"))
            } else {
                postRepository.deleteById(postId)
                return ResponseEntity.ok(mapOf("message" to "Post deleted"))
            }
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Could not delete post"))
        }
    }

    fun getAllPosts(page: Int, size: Int, userId: Long): Page<PostsOnMainPageResponseDTO> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postTime"))
        val postsPage = postRepository.findAllByOrderByPostTimeDesc(pageable)

        val user = userRepository.findById(userId).orElse(null)

        return postsPage.map { post ->
            PostsOnMainPageResponseDTO(
                id = post.id!!,
                image = post.image,
                text = post.text,
                user = UserSearchResponseDTO(
                    id = post.user.id!!,
                    username = post.user.username,
                    avatar = post.user.avatar
                ),
                postTime = post.postTime,
                // todo: comments
                commentsCount = post.comments.count(),
                likesCount = post.likes.count(),
                likedByUser = likeRepository.existsByUserAndPost(user, post),
            )
        }
    }
}