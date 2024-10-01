package my.project.petsnap.service

import my.project.petsnap.entity.LikeDB
import my.project.petsnap.repository.LikeRepository
import org.springframework.stereotype.Service

@Service
class LikeService(private val likeRepository: LikeRepository) {

    fun addLike(like: LikeDB): LikeDB {
        return likeRepository.save(like)
    }

    fun removeLike(likeId: Long) {
        likeRepository.deleteById(likeId)
    }

}