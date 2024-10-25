package my.project.petsnap.service

import jakarta.persistence.EntityNotFoundException
import my.project.petsnap.dto.LikeResponseDTO
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
    fun addOrRemoveLike(postId: Long, userId: Long): ResponseEntity<Any> {

        val user = userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }
        val post = postRepository.findById(postId).orElseThrow { EntityNotFoundException("Post not found") }

        val likedByUser = likeRepository.existsByUserAndPost(user, post)

        val likeResponse = LikeResponseDTO(
            likesCount = post.likes.count()
        )

        if (likedByUser) {
            // remove like
            val like = likeRepository.findByUserAndPost(user, post)
            user.likes.remove(like)  // удалить лайк в userDB
            post.likes.remove(like) // удалить лайк в postDB
            likeRepository.delete(like) // удалить likeDB
            likeResponse.likesCount--

        } else {
            // add like
            val like = LikeDB(user = user, post = post)
            likeRepository.save(like)
            likeResponse.likesCount++
        }

        return ResponseEntity.ok(likeResponse)


    }

}