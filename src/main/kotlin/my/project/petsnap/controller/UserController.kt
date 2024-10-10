package my.project.petsnap.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import my.project.petsnap.dto.LoginRequestDTO
import my.project.petsnap.dto.InputUserInfoRequestDTO
import my.project.petsnap.entity.UserDB
import my.project.petsnap.exception.FileEmptyException
import my.project.petsnap.service.JwtTokenService
import my.project.petsnap.service.UserService
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import org.slf4j.LoggerFactory
import org.slf4j.Logger


@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "Operations pertaining to users")
class UserController(
    private val userService: UserService,
    @Value("\${upload.path}") private val uploadPath: String,
    private val jwtTokenService: JwtTokenService,

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

        val user = changeUserInfo(username, password, birthday, bio, file)

        userService.register(user)
        logger.info("User registered successfully: $username")
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user")
    fun login(@RequestBody loginRequest: LoginRequestDTO): ResponseEntity<Any> {
        val user = userService.login(loginRequest.username, loginRequest.password)
        if (user != null) {
            val token = jwtTokenService.generateToken(user.username)
            return ResponseEntity.ok(mapOf("token" to token))
            // mapOf - функция, которая создает неизменяемый словарь (Map) в Kotlin. Она принимает пары "ключ-значение" и возвращает объект типа Map<K, V>, где K — тип ключа, а V — тип значения. В данном случае, ключом является строка "token", а значением — переменная token. Оператор to в Kotlin используется для создания пары (Pair).
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "User doesn't exist"))
        }
    }

    @GetMapping("/userProfile/{userId}")
    @Operation(summary = "Get user profile")
    fun getProfile(@Parameter(description = "User ID", required = true) @PathVariable userId: Long,): ResponseEntity<Any> {
        val existingUser = userService.getUserProfile(userId)
        return if (existingUser != null) {
            ResponseEntity.ok(existingUser)
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

        val editedUser = changeUserInfo(username, password, birthday, bio, file)
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
            userService.followUser(followerId, followingId)
            ResponseEntity.ok(mapOf("message" to "User followed successfully"))
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
            val result = userService.unfollowUser(followerId, followingId) // узнать, подписан ли пользователь на другого пользователя
            if (result) {
                ResponseEntity.ok(mapOf("message" to "User unfollowed successfully"))
            } else ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "This user is not in your following list")) // если не подписан, то не нужно отписаться
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

    // check birthday input format
    private fun isValidDateFormat(date: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            LocalDate.parse(date, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    // check if uploaded file is an  image file
    private fun isImageFile(file: MultipartFile): Boolean {
        val contentType = file.contentType
        return contentType != null && contentType.startsWith("image")
    }

    // generate url from image
    private fun generateUrl(file: MultipartFile): String {

        if (file.isEmpty) {
            throw FileEmptyException("File is empty")
        }

        return try {
            // UUID: Universally Unique Identifier -- 32 hexadecimal digits + file type
            val fileName = UUID.randomUUID().toString() + "." + file.originalFilename?.substringAfterLast(".")
            // filePath = uploadPath/filename
            val filePath = Paths.get(uploadPath, fileName)
            // copy the file from inputStream to filePath
            Files.copy(file.inputStream, filePath)

            // create a URL for imageFile
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString()
        } catch (e: Exception) {
            throw RuntimeException("Failed to upload file", e)
        }
    }

    // change userInfo
    private fun changeUserInfo(username: String, password: String, birthday: String, bio: String?, file: MultipartFile): InputUserInfoRequestDTO {

        // check birthday
        if (!isValidDateFormat(birthday)) {
            logger.error("Invalid birthday format: $birthday")
            ResponseEntity.badRequest()
                .body(mapOf("message" to "Invalid birthday format. Expected format: yyyy-MM-dd"))
        }

        // check if user exists
        val existingUser = userService.searchUser(username)
        if (existingUser != null) {
            logger.error("User already exists: $username")
            ResponseEntity.badRequest().body(mapOf("message" to "This user already exists"))
        }

        // check if file is an image
        if (!isImageFile(file)) {
            logger.error("Uploaded file is not an image: ${file.originalFilename}")
            ResponseEntity.badRequest().body(mapOf("message" to "Uploaded file is not an image"))
        }

        // generate image url
        val avatarUrl = generateUrl(file)
        logger.info("Generated avatar URL: $avatarUrl")

        // change birthday String to localDate
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDateBirthday = LocalDate.parse(birthday, formatter)
        logger.info("Parsed birthday: $localDateBirthday")

        val user = InputUserInfoRequestDTO(
            username = username,
            password = password,
            birthday = localDateBirthday,
            avatar = avatarUrl,
            bio = bio
        )
        return user
    }

}