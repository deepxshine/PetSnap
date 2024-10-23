package com.example.petsnap.di

import com.example.petsnap.data.remote.PostService
import com.example.petsnap.data.repository.PostRepositoryImpl
import com.example.petsnap.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostRepositoryModule {

    @Provides
    @Singleton
    fun providePostRepository(postService: PostService) : PostRepository {
        return PostRepositoryImpl(postService)
    }
}