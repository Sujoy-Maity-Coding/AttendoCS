package com.sujoy.pbcadmin.domain.UseCase

import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveRoutineDataUseCase @Inject constructor(private val repo: Repo) {
    suspend fun saveRoutineData(regYear: String, dept: String, sem: String, imageUrl: String): Flow<ResultState<String>> =
        repo.saveRoutineData(regYear, dept, sem, imageUrl)
}