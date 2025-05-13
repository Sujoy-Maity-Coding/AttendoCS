package com.sujoy.pbc.domain.usecase

import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWebUrlUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getWebUrl(regYr: String, department: String, semester: String): Flow<ResultState<String?>> =
        repo.getWebUrl(regYr,department,semester)
}