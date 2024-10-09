package my.project.petsnap.controller

import my.project.petsnap.entity.UserDB
import my.project.petsnap.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    @Value("\${upload.path}") private val uploadPath: String,
) {

    @PostMapping("/register")
    fun register(
        @RequestParam username: String,
        @RequestParam password: String,
        @RequestParam birthday: LocalDate,
        @RequestParam bio: String?,
        @RequestParam file: MultipartFile
    ): ResponseEntity<String> {

        if (!isImageFile(file)) {
            throw IllegalArgumentException("Uploaded file is not an image")
        }

        val avatarUrl = if (!file.isEmpty) {
            try {
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
        } else {
            throw IllegalArgumentException("File is empty")
        }

        val user = UserDB(
            username = username,
            password = password,
            birthday = birthday,
            avatar = avatarUrl,
            bio = bio
        )

        userService.register(user)
        return ResponseEntity.ok().build()
    }

    //    check if uploaded file is an  image file
    private fun isImageFile(file: MultipartFile): Boolean {
        val contentType = file.contentType
        return contentType != null && contentType.startsWith("image")
    }

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): Boolean {
        // TODO return session for user
        return userService.login(username, password)
    }

    @PutMapping("/editProfile")
    fun editProfile(@RequestBody user: UserDB): UserDB {
        return userService.editProfile(user)
    }

    @GetMapping("/search")
    fun searchUser(@RequestParam username: String): UserDB? {
        return userService.searchUser(username)
    }

    @PostMapping("/follow")
    fun followUser(@RequestParam followerId: Long, @RequestParam followingId: Long) {
        userService.followUser(followerId, followingId)
    }

    @PostMapping("/unfollow")
    fun unfollowUser(@RequestParam followerId: Long, @RequestParam followingId: Long) {
        userService.unfollowUser(followerId, followingId)
    }

    @GetMapping("/{userId}/followers")
    fun getFollowers(@PathVariable userId: Long): Set<UserDB> {
        return userService.getFollowers(userId)
    }

    @GetMapping("/{userId}/following")
    fun getFollowing(@PathVariable userId: Long): Set<UserDB> {
        return userService.getFollowing(userId)
    }
}