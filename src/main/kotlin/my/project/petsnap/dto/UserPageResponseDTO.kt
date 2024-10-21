package my.project.petsnap.dto

data class UserPageResponseDTO(
    val id: Long,
    val username: String,
    val avatar: String?,
    val bio: String?,
    val posts: List<PostResponseDTO>,
)
