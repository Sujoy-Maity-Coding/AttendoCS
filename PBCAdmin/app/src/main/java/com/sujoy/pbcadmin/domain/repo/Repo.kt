package com.sujoy.pbcadmin.domain.repo

import android.net.Uri
import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.model.ClassRoutine
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun addSemesterWebUrl(year: String, department: String, semester: String, webUrl: String): Flow<ResultState<String>>
    suspend fun uploadRoutineImage(regYear: String, dept: String, sem: String, imageUri: Uri): Flow<ResultState<String>>
    suspend fun saveRoutineData(regYear: String, dept: String, sem: String, imageUrl: String): Flow<ResultState<String>>
    fun addRoutineData(regYear: String, dept: String, sem: String, routine: ClassRoutine): Flow<ResultState<String>>
}