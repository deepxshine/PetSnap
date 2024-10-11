package my.project.petsnap.repository

import my.project.petsnap.entity.FriendshipDB
import org.springframework.data.jpa.repository.JpaRepository

interface FriendshipRepository : JpaRepository<FriendshipDB, Long> {
    fun findByFollowerIdAndFollowingId(followerId: Long, followingId: Long): FriendshipDB? // determine if friendship exists

    fun findByFollowerId(followerId: Long): List<FriendshipDB> // find followings

    fun findByFollowingId(followingId: Long): List<FriendshipDB> // find followers
}