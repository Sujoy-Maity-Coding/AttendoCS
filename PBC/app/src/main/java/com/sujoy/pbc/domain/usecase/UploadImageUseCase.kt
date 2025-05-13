package com.sujoy.pbc.domain.usecase

import android.content.Context
import android.net.Uri
import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.UserDataParent
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val repo: Repo) {
    fun uploadImage(imageUri: Uri, userId: String): Flow<ResultState<String>> =  repo.uploadImageToFirebase(imageUri, userId)
}