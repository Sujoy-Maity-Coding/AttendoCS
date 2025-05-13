package com.sujoy.pbccs.domain.UseCase

import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWebUrlUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getWebUrl(regYr: String, department: String, semester: String): Flow<ResultState<String?>> =
        repo.getWebUrl(regYr,department,semester)
}