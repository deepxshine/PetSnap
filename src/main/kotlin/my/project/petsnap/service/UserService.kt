package my.project.petsnap.service

import my.project.petsnap.dto.*
import my.project.petsnap.entity.FriendshipDB
import my.project.petsnap.entity.UserDB
import my.project.petsnap.exception.UserNotFoundException
import my.project.petsnap.repository.FriendshipRepository
import my.project.petsnap.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun register(user: InputUserInfoRequestDTO) {
        val userData = UserDB(
            username = user.username,
            password = passwordEncoder.encode(user.password),
            birthday = user.birthday,
            avatar = user.avatar,
            bio = user.bio
        )
        userRepository.save(userData)
    }

    fun login(username: String, password: String): UserDB? {
        val user = userRepository.findByUsername(username).orElse(null)
        return if (user != null && passwordEncoder.matches(
                password,
                user.password
            )
        ) { // если пользователь существует в базе и пароль введенный соответствует с паролью в базе, то возвращать нужную информацию
            user
        } else {
            null
        }
    }

    fun editProfile(userId: Long, userInfo: InputUserInfoRequestDTO) {
        val existingUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User not found") }
        // Обновить данные пользователя
        existingUser.username = userInfo.username
        existingUser.password = userInfo.password
        existingUser.birthday = userInfo.birthday
        existingUser.avatar = userInfo.avatar
        existingUser.bio = userInfo.bio

        userRepository.save(existingUser)
    }

    fun searchUser(username: String): UserSearchResponseDTO? {
        val existingUser = userRepository.findByUsername(username).orElse(null)
        if (existingUser != null) {
            val userData = UserSearchResponseDTO(
                id = existingUser.id!!,
                username = existingUser.username,
                avatar = existingUser.avatar
            )
            return userData
        } else {
            return null
        }

    }

    fun getUserProfile(userId: Long): UserPageResponseDTO? {
        val existingUser = userRepository.findById(userId).orElse(null)
        return if (existingUser != null) {
            val userInfo = UserPageResponseDTO(
                id = existingUser.id!!,
                username = existingUser.username,
                avatar = existingUser.avatar,
                bio = existingUser.bio,
                posts = existingUser.posts,
                likes = existingUser.likes,
                comments = existingUser.comments
            )
            userInfo
        } else {
            null
        }
    }

    fun followUser(followerId: Long, followingId: Long) {
        val follower = userRepository.findById(followerId).orElseThrow { RuntimeException("Follower not found") }
        val following =
            userRepository.findById(followingId).orElseThrow { RuntimeException("Following not found") }

        // Если не подписан, то follower подписывается на following
        val existingFriendship = friendshipRepository.findByFollowerIdAndFollowingId(followerId, followingId)
        if (existingFriendship == null) {
            val friendship = FriendshipDB(follower = follower, following = following)
            friendshipRepository.save(friendship)
        }
    }

    fun unfollowUser(followerId: Long, followingId: Long): Boolean {
        val existingFriendship = friendshipRepository.findByFollowerIdAndFollowingId(followerId, followingId)
        if (existingFriendship != null) {
            friendshipRepository.delete(existingFriendship)
            return true
        } else {
            return false
        }
    }

    fun getFollowers(followingId: Long): List<FollowDTO> {
        val friendships = friendshipRepository.findByFollowingId(followingId)
        val followers = friendships.map { it.follower }
        val followersList = followers.map { follower ->
            FollowDTO(
                id = follower.id!!,
                username = follower.username,
                avatar = follower.avatar
            )
        }
        return followersList
    }

    fun getFollowings(followerId: Long): List<FollowDTO> {
        val friendships = friendshipRepository.findByFollowerId(followerId)
        val followings = friendships.map { it.following }
        val followingsList = followings.map { following ->
            FollowDTO(
                id = following.id!!,
                username = following.username,
                avatar = following.avatar
            )
        }
        return followingsList
    }

}

