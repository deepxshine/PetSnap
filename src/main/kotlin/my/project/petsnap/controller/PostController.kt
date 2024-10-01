package my.project.petsnap.controller

import my.project.petsnap.dto.PostWithDetailsDTO
import my.project.petsnap.entity.PostDB
import my.project.petsnap.service.PostService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(private val postService: PostService) {

    @GetMapping("/{postId}")
    fun getPostWithDetails(@PathVariable postId: Long): PostWithDetailsDTO {
        return postService.getPostWithLikesAndComments(postId)
    }

    @GetMapping("/viewPosts/{userId}")
    fun getPostsByUserId(@PathVariable userId: Long): List<PostDB> {
        return postService.getPostsByUserId(userId)
    }

    @PostMapping
    fun createPost(@RequestBody post: PostDB): PostDB {
        return postService.createPost(post)
    }

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Long) {
        postService.deletePost(postId)
    }
}