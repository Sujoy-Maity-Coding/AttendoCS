package com.sujoy.pbccs.domain.di

import com.google.firebase.firestore.FirebaseFirestore
import com.sujoy.pbccs.Data.api.AttendanceApi
import com.sujoy.pbccs.Data.repo.RepoImpl
import com.sujoy.pbccs.domain.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UiModule {
    @Provides
    fun provideRepo(firestore: FirebaseFirestore): Repo {
        return RepoImpl(firestore = firestore)
    }
}