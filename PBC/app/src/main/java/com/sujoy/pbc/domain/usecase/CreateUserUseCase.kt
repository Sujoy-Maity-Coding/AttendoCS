package com.sujoy.pbc.domain.usecase

import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repo: Repo) {
    fun createUser(userData: UserData, year: String, department: String): Flow<ResultState<String>> {
        return repo.registerUserWithEmailPassword(userData, year, department)
    }
}