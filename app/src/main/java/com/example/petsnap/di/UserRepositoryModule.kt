package com.example.petsnap.di

import com.example.petsnap.data.remote.RegisterService
import com.example.petsnap.data.repository.UserRepositoryImpl
import com.example.petsnap.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    fun provideUserRepository(registerService: RegisterService) : UserRepository {
        return UserRepositoryImpl(registerService)
    }
}