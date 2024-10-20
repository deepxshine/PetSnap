package my.project.petsnap.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import my.project.petsnap.dto.LoginRequestDTO
import my.project.petsnap.dto.InputUserInfoRequestDTO
import my.project.petsnap.service.JwtTokenService
import my.project.petsnap.service.UserService
import my.project.petsnap.utils.DateUtils
import my.project.petsnap.utils.ImageUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.slf4j.LoggerFactory
import org.slf4j.Logger


@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "Operations pertaining to users")
class UserController(
    private val userService: UserService,
    private val jwtTokenService: JwtTokenService,
    private val imageUtils: ImageUtils,
    ) {
    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/register", consumes = ["multipart/form-data"]) // для загрузки аватара
    @Operation(summary = "Register a new user")
    fun register(
        @Parameter(description = "Username", required = true) @RequestParam username: String,
        @Parameter(description = "Password", required = true) @RequestParam password: String,
        @Parameter(description = "Birthday", required = true) @RequestParam birthday: String,
        @Parameter(description = "Bio", required = false) @RequestParam bio: String?,
        @Parameter(description = "Avatar file", required = true) @RequestPart file: MultipartFile,
    ): ResponseEntity<Any> {

        val userInfoResponse = changeUserInfo(username, password, birthday, bio, file)

        if (userInfoResponse.statusCode != HttpStatus.OK) {
            return userInfoResponse
        }
        // если всё ок, то достанем userInfo из response
        val newUser = userInfoResponse.body as InputUserInfoRequestDTO // превращает response.entity<any> в InputUserInfoRequestDTO

        userService.register(newUser)
        logger.info("User registered successfully: $username")
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user")
    fun login(@RequestBody loginRequest: LoginRequestDTO): ResponseEntity<Any> {
        val user = userService.login(loginRequest.username, loginRequest.password)
        if (user != null) {
            val token = jwtTokenService.generateToken(user.username)
            return ResponseEntity.ok(mapOf("token" to token, "userId" to user.id))
            // mapOf - функция, которая создает неизменяемый словарь (Map) в Kotlin. Она принимает пары "ключ-значение" и возвращает объект типа Map<K, V>, где K — тип ключа, а V — тип значения. В данном случае, ключом является строка "token", а значением — переменная token. Оператор to в Kotlin используется для создания пары (Pair).
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "User doesn't exist"))
        }
    }

    @GetMapping("/userPage/{userId}")
    @Operation(summary = "Get user page")
    fun getUserPage(
        @Parameter(description = "User ID", required = true) @PathVariable userId: Long,
        @Parameter(description = "Page number", required = false) @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Page size", required = false) @RequestParam(defaultValue = "9") size: Int
    ): ResponseEntity<Any> {
        val userPage = userService.getUserPage(userId, page, size)
        return if (userPage != null) {
            ResponseEntity.ok(userPage)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "User not found"))
        }
    }

    @PutMapping("/editProfile/{userId}", consumes = ["multipart/form-data"])
    @Operation(summary = "Edit user profile")
    fun editProfile(
        @Parameter(description = "Username", required = true) @RequestParam username: String,
        @Parameter(description = "Password", required = true) @RequestParam password: String,
        @Parameter(description = "Birthday", required = true) @RequestParam birthday: String,
        @Parameter(description = "Bio", required = false) @RequestParam bio: String?,
        @Parameter(description = "Avatar file", required = true) @RequestPart file: MultipartFile,
        @Parameter(description = "User ID", required = true) @PathVariable userId: Long,
    ): ResponseEntity<Any> {

        val userInfoResponse = changeUserInfo(username, password, birthday, bio, file)

        if (userInfoResponse.statusCode != HttpStatus.OK) {
            return userInfoResponse
        }
        // если всё ок, то достанем userInfo из response
        val editedUser = userInfoResponse.body as InputUserInfoRequestDTO

        userService.editProfile(userId, editedUser)
        return ResponseEntity.ok(mapOf("message" to "Profile edited successfully"))
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a user by username")
    fun searchUser(
        @Parameter(description = "Username", required = true) @RequestParam username: String,
    ): ResponseEntity<Any> {
        val existingUser = userService.searchUser(username)
        return if (existingUser != null) {
            ResponseEntity.ok(existingUser)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "User not found"))
        }
    }

    @PostMapping("/follow/{followerId}/{followingId}")
    @Operation(summary = "Follow a user")
    fun followUser(
        @Parameter(description = "Follower ID", required = true) @PathVariable followerId: Long,
        @Parameter(description = "Following ID", required = true) @PathVariable followingId: Long,
    ): ResponseEntity<Any> {
        return try {
            if (followingId == followerId) {
                ResponseEntity.badRequest().body(mapOf("error" to "You can not follow yourself"))
            } else {
                val result = userService.followUser(followerId, followingId)
                if (result) {
                    ResponseEntity.ok(mapOf("message" to "User followed successfully"))
                } else ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("error" to "This user is already in your following list")) // если подписан, то не нужно повторно подписаться
            }
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/unfollow/{followerId}/{followingId}")
    @Operation(summary = "Unfollow a user")
    fun unfollowUser(
        @Parameter(description = "Follower ID", required = true) @PathVariable followerId: Long,
        @Parameter(description = "Following ID", required = true) @PathVariable followingId: Long,
    ): ResponseEntity<Any> {
        return try {
            if (followingId == followerId) {
                ResponseEntity.badRequest().body(mapOf("error" to "You can not unfollow yourself"))
            } else {
                val result = userService.unfollowUser(
                    followerId,
                    followingId
                ) // узнать, подписан ли пользователь на другого пользователя
                if (result) {
                    ResponseEntity.ok(mapOf("message" to "User unfollowed successfully"))
                } else ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("error" to "This user is not in your following list")) // если не подписан, то не нужно отписаться
            }
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "Get user's followers")
    fun getFollowers(
        @Parameter(description = "User ID", required = true) @PathVariable userId: Long,
    ): ResponseEntity<Any> {
        val followersList = userService.getFollowers(userId)
        return ResponseEntity.ok(followersList)
    }

    @GetMapping("/{userId}/followings")
    @Operation(summary = "Get user's followings")
    fun getFollowing(
        @Parameter(description = "User ID", required = true) @PathVariable userId: Long,
    ): ResponseEntity<Any> {
        val followingsList = userService.getFollowings(userId)
        return ResponseEntity.ok(followingsList)
    }

    // change userInfo
    private fun changeUserInfo(username: String, password: String, birthday: String, bio: String?, file: MultipartFile): ResponseEntity<Any> {

        // check birthday
        if (!DateUtils.isValidDateFormat(birthday)) {
            return ResponseEntity.badRequest()
                .body(mapOf("message" to "Invalid birthday format. Expected format: yyyy-MM-dd"))
        }

        // check if user exists
        val existingUser = userService.searchUser(username)
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(mapOf("message" to "This user already exists"))
        }

        // check if file is an image
        if (!imageUtils.isImageFile(file)) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Uploaded file is not an image"))
        }

        // generate image url
        val avatarUrl = imageUtils.generateUrl(file)

        // change birthday String to localDate
        val localDateBirthday = DateUtils.changeDateFormat(birthday)

        // generate user info
        val userInfo = InputUserInfoRequestDTO(
            username = username,
            password = password,
            birthday = localDateBirthday,
            avatar = avatarUrl,
            bio = bio
        )
        return ResponseEntity.ok(userInfo)
    }

}