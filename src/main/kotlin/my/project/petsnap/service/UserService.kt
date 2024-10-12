package my.project.petsnap.service

import my.project.petsnap.dto.*
import my.project.petsnap.entity.FriendshipDB
import my.project.petsnap.entity.UserDB
import my.project.petsnap.exception.UserNotFoundException
import my.project.petsnap.repository.FriendshipRepository
import my.project.petsnap.repository.PostRepository
import my.project.petsnap.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
    private val passwordEncoder: PasswordEncoder,
    private val postRepository: PostRepository,
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
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User not found") }
        // Обновить данные пользователя
        user.username = userInfo.username
        user.password = passwordEncoder.encode(userInfo.password)
        user.birthday = userInfo.birthday
        user.avatar = userInfo.avatar
        user.bio = userInfo.bio

        userRepository.save(user)
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

    fun getUserPage(userId: Long, page: Int, size: Int): UserPageResponseDTO? {
        val user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            return null
        } else {
            // get all posts by PostTime Descending, and paginate them
            val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postTime"))
            val postsPage = postRepository.findByUser(user, pageable)

            val postsList = postsPage.content.map { post ->
                PostResponseDTO(
                    id = post.id!!,
                    image = post.image,
                    text = post.text,
                    postTime = post.postTime,
                    comments = post.comments,
                    likes = post.likes,
                )

            }

            return UserPageResponseDTO(
                id = user.id!!,
                username = user.username,
                avatar = user.avatar,
                bio = user.bio,
                posts = postsList,
                likes = user.likes,
                comments = user.comments
            )
        }
    }


    fun followUser(followerId: Long, followingId: Long): Boolean {
        val follower = userRepository.findById(followerId).orElseThrow { RuntimeException("Follower not found") }
        val following =
            userRepository.findById(followingId).orElseThrow { RuntimeException("Following not found") }

        // Если не подписан, то follower подписывается на following
        val existingFriendship = friendshipRepository.findByFollowerIdAndFollowingId(followerId, followingId)
        if (existingFriendship == null) {
            val friendship = FriendshipDB(follower = follower, following = following)
            friendshipRepository.save(friendship)
            return true
        } else {
            return false
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

