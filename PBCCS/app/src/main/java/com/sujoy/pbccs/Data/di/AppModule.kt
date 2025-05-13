package com.sujoy.pbccs.Data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.sujoy.pbccs.Common.Constant.Constant
import com.sujoy.pbccs.Data.api.AttendanceApi
import com.sujoy.pbccs.Data.repo.RepoImpl
import com.sujoy.pbccs.domain.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
