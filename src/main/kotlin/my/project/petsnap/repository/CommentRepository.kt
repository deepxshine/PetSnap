package my.project.petsnap.repository

import my.project.petsnap.entity.CommentDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<CommentDB, Long> {
    fun findCommentsByPostId(postId: Long): List<CommentDB>
}