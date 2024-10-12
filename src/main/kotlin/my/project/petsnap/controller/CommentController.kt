package my.project.petsnap.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import my.project.petsnap.service.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
@Tag(name = "Comments API", description = "Operations for comments")
class CommentController(private val commentService: CommentService) {


    @GetMapping("/{postId}/{userId}")
    @Operation(summary = "Get post comments")
    fun getPostComments(
        @Parameter(description = "Post ID", required = true) @PathVariable postId: Long,
        @Parameter(description = "User ID", required = true) @PathVariable userId: Long,
        @Parameter(description = "Page number", required = false) @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Page size", required = false) @RequestParam(defaultValue = "9") size: Int
    ): ResponseEntity<Any> {
        return commentService.getCommentsByPostId(postId, userId, page, size)
    }

    @PostMapping("/addComment/{userId}/{postId}")
    @Operation(summary = "Add a comment")
    fun addComment(
        @Parameter(description = "USER Id", required = true) @PathVariable userId: Long,
        @Parameter(description = "POST Id", required = true) @PathVariable postId: Long,
        @Parameter(description = "Comment Content", required = true) @RequestBody comment: String,
    ): ResponseEntity<Any> {
        return commentService.addComment(userId, postId, comment)
    }

    @DeleteMapping("/removeComment/{userId}/{commentId}")
    @Operation(summary = "Remove a comment")
    fun removeComment(
        @Parameter(description = "USER Id", required = true) @PathVariable userId: Long,
        @Parameter(description = "COMMENT Id", required = true) @PathVariable commentId: Long,
    ): ResponseEntity<Any> {
        return commentService.removeComment(userId, commentId)
    }

}