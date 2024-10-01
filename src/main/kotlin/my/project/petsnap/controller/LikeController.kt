package my.project.petsnap.controller

import my.project.petsnap.entity.LikeDB
import my.project.petsnap.service.LikeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(private val likeService: LikeService) {

    @PutMapping("/addLike")
    fun addLike(@RequestBody like: LikeDB): LikeDB {
        return likeService.addLike(like)
    }

    @DeleteMapping("/{likeId}")
    fun removeLike(@PathVariable likeId: Long) {
        likeService.removeLike(likeId)
    }

}