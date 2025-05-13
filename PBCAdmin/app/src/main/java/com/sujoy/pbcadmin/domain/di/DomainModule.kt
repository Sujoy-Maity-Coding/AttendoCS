package com.sujoy.pbcadmin.domain.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sujoy.pbcadmin.data.repo.RepoImpl
import com.sujoy.pbcadmin.domain.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideRepo(firebaseFirestore: FirebaseFirestore,firebaseStorage: FirebaseStorage): Repo {
        return RepoImpl(firebaseFirestore,firebaseStorage)
    }

}