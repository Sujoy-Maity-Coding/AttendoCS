package com.sujoy.pbc.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sujoy.pbc.data.repo.RepoImpl
import com.sujoy.pbc.domain.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideRepo(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore, firebaseStorage: FirebaseStorage): Repo {
        return RepoImpl(firebaseAuth,firebaseFirestore,firebaseStorage)
    }

}