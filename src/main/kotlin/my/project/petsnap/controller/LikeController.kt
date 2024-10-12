package my.project.petsnap.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import my.project.petsnap.entity.LikeDB
import my.project.petsnap.service.LikeService
import my.project.petsnap.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/likes")
@Tag(name = "Like API", description = "Operations pertaining to likes")
class LikeController(private val likeService: LikeService
) {

    @PutMapping("/addLike/{postId}/{userId}")
    @Operation(summary = "like a post")
    fun addLike(
        @Parameter(description = "POST Id", required = true) @PathVariable postId: Long,
        @Parameter(description = "USER Id", required = true) @PathVariable userId: Long,
    ): ResponseEntity<Any> {
        return likeService.addLike(postId, userId)
    }

    @DeleteMapping("/removeLike/{postId}/{userId}")
    fun removeLike(
        @Parameter(description = "POST Id", required = true) @PathVariable postId: Long,
        @Parameter(description = "USER Id", required = true) @PathVariable userId: Long,
    ): ResponseEntity<Any> {
        return likeService.removeLike(postId, userId)
    }

}