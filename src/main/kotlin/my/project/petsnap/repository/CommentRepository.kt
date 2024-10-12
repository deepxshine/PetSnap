package my.project.petsnap.repository

import my.project.petsnap.entity.CommentDB
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<CommentDB, Long> {

    fun findCommentDBByPostIdOrderByCommentTimeDesc (postId: Long, pageable: Pageable): Page<CommentDB>

}