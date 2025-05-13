package com.sujoy.pbccs.domain.UseCase

import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.model.StudentData
import com.sujoy.pbccs.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudenyByIdUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getStudentById(regYr: String, department: String, idNo: String): Flow<ResultState<StudentData?>> =
        repo.getStudentById(regYr, department, idNo)
}