package com.sujoy.pbc.domain.usecase

import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.UserDataParent
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repo: Repo) {
    fun getUserByUId(uId: String,regYr:String,dept:String): Flow<ResultState<UserDataParent>> =
        repo.getUserByUId(uId,regYr,dept)
}