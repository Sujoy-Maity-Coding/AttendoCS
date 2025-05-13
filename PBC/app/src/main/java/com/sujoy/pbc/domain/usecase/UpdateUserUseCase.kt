package com.sujoy.pbc.domain.usecase

import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.UserDataParent
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val repo: Repo) {
    fun updateUserData(userDataParent: UserDataParent, regYr:String, dept:String): Flow<ResultState<UserDataParent>> =
        repo.updateUserData(userDataParent, regYr, dept)
}