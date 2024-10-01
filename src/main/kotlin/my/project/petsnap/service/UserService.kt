package my.project.petsnap.service

import my.project.petsnap.entity.UserDB
import my.project.petsnap.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun register(user: UserDB): UserDB {
        return userRepository.save(user)
    }

    fun login(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username)
        return user != null && user.password == password
    }

    fun editProfile(user: UserDB): UserDB {
        return userRepository.save(user)
    }

    fun searchUser(username: String): UserDB? {
        return userRepository.findByUsername(username)
    }

    fun followUser(followerId: Long, followingId: Long) {
        val follower = userRepository.findById(followerId).orElseThrow { RuntimeException("Follower not found") }
        val following = userRepository.findById(followingId).orElseThrow { RuntimeException("Following not found") }

        // Если не подписан, то follower подписывается на following
        if (!follower.following.contains(following)) {
            follower.following.add(following)
            following.followers.add(follower)

            userRepository.save(follower)
            userRepository.save(following)
        }
    }

    fun unfollowUser(followerId: Long, followingId: Long) {
        val follower = userRepository.findById(followerId).orElseThrow { RuntimeException("Follower not found") }
        val following = userRepository.findById(followingId).orElseThrow { RuntimeException("Following not found") }

        // Если подписан, то follower отписывается на following
        if(follower.following.contains(following)) {
            follower.following.remove(following)
            following.followers.remove(follower)

            userRepository.save(follower)
            userRepository.save(following)
        }
    }

    fun getFollowers(userId: Long): Set<UserDB> {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        return user.followers
    }

    fun getFollowing(userId: Long): Set<UserDB> {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        return user.following
    }
}