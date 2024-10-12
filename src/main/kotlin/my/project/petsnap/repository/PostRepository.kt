package my.project.petsnap.repository

import my.project.petsnap.entity.PostDB
import my.project.petsnap.entity.UserDB
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<PostDB, Long> {

    fun findAllByOrderByPostTimeDesc(pageable: Pageable): Page<PostDB>

    fun findByUser(user: UserDB, pageable: Pageable): Page<PostDB>

}