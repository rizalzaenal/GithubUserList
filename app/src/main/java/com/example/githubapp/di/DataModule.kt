package com.example.githubapp.di

import com.example.githubapp.data.remote.GithubService
import com.example.githubapp.data.repository.UserRepository
import com.example.githubapp.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideUserRepository(githubService: GithubService): UserRepository {
        return UserRepositoryImpl(githubService)
    }
}