package com.sujoy.pbcadmin.domain.UseCase

import android.net.Uri
import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadImageRoutineUseCase @Inject constructor(private val repo: Repo) {
    suspend fun uploadRoutineImage(regYear: String, dept: String, sem: String, imageUri: Uri): Flow<ResultState<String>> = repo.uploadRoutineImage(regYear, dept, sem, imageUri)
}