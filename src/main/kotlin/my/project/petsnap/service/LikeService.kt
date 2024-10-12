package my.project.petsnap.service

import jakarta.persistence.EntityNotFoundException
import my.project.petsnap.dto.LikeResponseDTO
import my.project.petsnap.dto.PostDTO
import my.project.petsnap.dto.UserSearchResponseDTO
import my.project.petsnap.entity.LikeDB
import my.project.petsnap.repository.LikeRepository
import my.project.petsnap.repository.PostRepository
import my.project.petsnap.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,

    ) {
    @Transactional
    fun addLike(postId: Long, userId: Long): ResponseEntity<Any> {

        val user = userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }
        val post = postRepository.findById(postId).orElseThrow { EntityNotFoundException("Post not found") }

        if (likeRepository.existsByUserAndPost(user, post)) {
            return ResponseEntity.badRequest().body("User has already liked this post")
        }

        val like = LikeDB(user = user, post = post)
        likeRepository.save(like)
        val likeResponse = LikeResponseDTO(
            id = like.id!!,
            user = UserSearchResponseDTO(
                id = like.user.id!!,
                username = like.user.username,
                avatar = like.user.avatar,
            ),
            post = PostDTO(
                id = like.post.id!!,
                image = like.post.image,
                text = like.post.text,
            )
        )
        return ResponseEntity.ok(mapOf("message" to "Liked successfully", "like" to likeResponse))
    }

    @Transactional
    fun removeLike(postId: Long, userId: Long) : ResponseEntity<Any> {
        val user = userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }
        val post = postRepository.findById(postId).orElseThrow { EntityNotFoundException("Post not found") }

        val like = likeRepository.findByUserAndPost(user, post)
        if (like == null) {
            return ResponseEntity.badRequest().body("User has not liked this post")
        } else {
            likeRepository.delete(like)
            val likeRemoveResponse = LikeResponseDTO(
                id = like.id!!,
                user = UserSearchResponseDTO(
                    id = like.user.id!!,
                    username = like.user.username,
                    avatar = like.user.avatar,
                ),
                post = PostDTO(
                    id = like.post.id!!,
                    image = like.post.image,
                    text = like.post.text,
                )
            )
            return ResponseEntity.ok(mapOf("message" to "Removed like", "Removed" to likeRemoveResponse))
        }
    }

}