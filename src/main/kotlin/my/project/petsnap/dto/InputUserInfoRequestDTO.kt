package my.project.petsnap.dto

import java.time.LocalDate

data class InputUserInfoRequestDTO(
    val username: String,
    val password: String,
    val birthday: LocalDate,
    val bio: String?,
    val avatar: String
)