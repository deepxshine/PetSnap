package my.project.petsnap.dto

data class LikeResponseDTO(
    val id: Long,
    val user: UserSearchResponseDTO,
    val post: PostDTO,
)
