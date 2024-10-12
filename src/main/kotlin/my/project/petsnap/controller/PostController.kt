package my.project.petsnap.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import my.project.petsnap.dto.PostResponseDTO
import my.project.petsnap.dto.PostsOnMainPageResponseDTO
import my.project.petsnap.entity.PostDB
import my.project.petsnap.service.PostService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/posts")
@Tag(name = "POST API", description = "Operations pertaining to posts")
class PostController(private val postService: PostService) {

    @PostMapping("/createPost/{userId}", consumes = ["multipart/form-data"]) // для загрузки фото
    @Operation(summary = "Create a new post")
    fun createPost(
        @Parameter(description = "Image", required = true) @RequestPart file: MultipartFile,
        @Parameter(description = "text", required = false) @RequestParam text: String?,
        @Parameter(description = "User ID", required = true) @PathVariable userId: Long,
    ): ResponseEntity<Any> {
        return postService.createPost(file, text, userId)
    }

    @DeleteMapping("/deletePost/{postId}")
    @Operation(summary = "Delete a post")
    fun deletePost(@Parameter(description = "POST ID", required = true) @PathVariable postId: Long) : ResponseEntity<Any> {
        return postService.deletePost(postId)
    }

    @GetMapping("/main") // Main page with users' newest posts
    @Operation(summary = "Show 5 newest posts on main page")
    fun getAllPosts(
        @Parameter(description = "Page number", required = false) @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Page size", required = false) @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<Page<PostsOnMainPageResponseDTO>> {
        val posts = postService.getAllPosts(page, size)
        return ResponseEntity.ok(posts)
    }

}