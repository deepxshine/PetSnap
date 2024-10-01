package my.project.petsnap.repository

import my.project.petsnap.entity.PostDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<PostDB, Long> {
    fun findByUserId(userId: Long): List<PostDB>

//    fun findByIdWithLikesAndComments(postId: Long): PostDB?
}