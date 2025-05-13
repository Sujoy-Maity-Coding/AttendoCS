package com.sujoy.pbc.domain.usecase

import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoutineUseCase @Inject constructor(private val repo: Repo) {
    fun getRoutineImageUrl(regYear: String, dept: String, sem: String): Flow<ResultState<String>> =
        repo.getRoutineImageUrl(regYear, dept, sem)
}