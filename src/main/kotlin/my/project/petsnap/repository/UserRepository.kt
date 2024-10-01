package my.project.petsnap.repository

import my.project.petsnap.entity.UserDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserDB, Long> {

    fun findByUsername(username: String): UserDB?
}
