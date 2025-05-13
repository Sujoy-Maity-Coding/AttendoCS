package com.sujoy.pbccs.domain.UseCase

import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.model.Student
import com.sujoy.pbccs.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchStudentsUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getStudents(sheet: String): Flow<ResultState<List<Student>>> = repo.getStudents(sheet = sheet)
}