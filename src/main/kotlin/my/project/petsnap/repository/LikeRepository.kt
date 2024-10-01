package my.project.petsnap.repository

import my.project.petsnap.entity.LikeDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository: JpaRepository<LikeDB, Long> {

}