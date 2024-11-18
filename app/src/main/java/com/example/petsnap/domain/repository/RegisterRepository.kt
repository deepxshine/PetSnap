package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.ResponseMsg
import com.example.petsnap.utils.Resource
import java.io.File

interface RegisterRepository {
    suspend fun registerUser(
        username: String,
        password: String,
        birthday: String?,
        bio: String?,
        file: File?
    ): Resource<ResponseMsg>
}