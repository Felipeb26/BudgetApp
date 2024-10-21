package com.batsworks.budget.di

import com.batsworks.budget.data.repository.FirebaseCollection
import com.batsworks.budget.data.entity.AmountFirebaseEntity
import com.batsworks.budget.data.entity.UserFirebaseEntity
import com.batsworks.budget.data.repository.CustomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepo(): CustomRepository<UserFirebaseEntity> {
        return CustomRepository(FirebaseCollection.USERS, FirebaseCollection.USERS.entity)
    }

    @Provides
    @Singleton
    fun provideAmountRepo(): CustomRepository<AmountFirebaseEntity> {
        return CustomRepository(FirebaseCollection.AMOUNTS, FirebaseCollection.AMOUNTS.entity)
    }

}