package com.sujoy.pbcadmin.domain.UseCase

import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.model.ClassRoutine
import com.sujoy.pbcadmin.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRoutineDataUseCase @Inject constructor(private val repo: Repo){
    fun addRoutineData(regYear: String, dept: String, sem: String, routine: ClassRoutine): Flow<ResultState<String>> =
        repo.addRoutineData(regYear, dept, sem, routine)
}