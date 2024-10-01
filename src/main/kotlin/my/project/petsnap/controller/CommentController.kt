package my.project.petsnap.controller

import my.project.petsnap.entity.CommentDB
import my.project.petsnap.service.CommentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentController(private val commentService: CommentService) {

    @PostMapping("/addComment")
    fun addComment(@RequestBody comment: CommentDB): CommentDB {
        return commentService.addComment(comment)
    }

    @DeleteMapping("/{commentId}")
    fun removeComment(@PathVariable commentId: Long) {
        commentService.removeComment(commentId)
    }

    @GetMapping("/posts/{postId}")
    fun getCommentsByPostId(@PathVariable postId: Long): List<CommentDB> {
        return commentService.getCommentsByPostId(postId)
    }
}