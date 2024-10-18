package com.example.petsnap.di

import com.example.petsnap.data.remote.RegisterService
import com.example.petsnap.data.remote.UserService
import com.example.petsnap.data.repository.RegisterRepositoryImpl
import com.example.petsnap.data.repository.UserProfileRepositoryImpl
import com.example.petsnap.domain.repository.RegisterRepository
import com.example.petsnap.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    fun provideUserRepository(registerService: RegisterService) : RegisterRepository {
        return RegisterRepositoryImpl(registerService)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(userService: UserService): UserProfileRepository {
        return UserProfileRepositoryImpl(userService)
    }
}