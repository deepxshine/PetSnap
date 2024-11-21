package com.example.petsnap.di

import com.example.petsnap.data.remote.CommentService
import com.example.petsnap.data.repository.CommentRepositoryImpl
import com.example.petsnap.domain.repository.CommentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommentRepositoryModule {

    @Provides
    @Singleton
    fun provideCommentRepository(commentService: CommentService) : CommentRepository {
        return CommentRepositoryImpl(commentService)
    }
}