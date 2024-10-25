package com.example.petsnap.di

import com.example.petsnap.data.remote.PostService
import com.example.petsnap.data.repository.LikeRepositoryImpl
import com.example.petsnap.domain.repository.LikeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LikeRepositoryModule {

    @Provides
    @Singleton
    fun provideLikeRepository(postService: PostService): LikeRepository {
        return LikeRepositoryImpl(postService)
    }
}