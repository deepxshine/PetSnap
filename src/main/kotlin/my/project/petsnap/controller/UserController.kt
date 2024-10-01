package my.project.petsnap.controller

import my.project.petsnap.entity.UserDB
import my.project.petsnap.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody user: UserDB): UserDB {
        return userService.register(user)
    }

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): Boolean {
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