package my.project.petsnap.repository

import my.project.petsnap.entity.LikeDB
import my.project.petsnap.entity.PostDB
import my.project.petsnap.entity.UserDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository: JpaRepository<LikeDB, Long> {

    fun existsByUserAndPost(user: UserDB, post: PostDB): Boolean

    fun findByUserAndPost(user: UserDB, post: PostDB): LikeDB?

}