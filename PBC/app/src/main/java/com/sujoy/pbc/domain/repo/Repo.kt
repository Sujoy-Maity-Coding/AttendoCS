package com.sujoy.pbc.domain.repo

import android.content.Context
import android.net.Uri
import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.AttendanceSummary
import com.sujoy.pbc.domain.model.ClassRoutine
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.domain.model.UserDataParent
import kotlinx.coroutines.flow.Flow
import java.io.File

interface Repo{
    fun registerUserWithEmailPassword(userData: UserData,year:String,department:String): Flow<ResultState<String>>
    fun signInWithEmailPassword(userData: UserData): Flow<ResultState<String>>
    fun uploadImageToFirebase(imageUri: Uri, userId: String): Flow<ResultState<String>>
    fun getUserByUId(uId: String,regYr:String,dept:String): Flow<ResultState<UserDataParent>>
    fun updateUserData(userDataParent: UserDataParent,regYr:String,dept:String): Flow<ResultState<UserDataParent>>
    fun getRoutineImageUrl(regYear: String, dept: String, sem: String): Flow<ResultState<String>>
    suspend fun getWebUrl(regYr: String, department: String, semester: String): Flow<ResultState<String?>>
    suspend fun getAttendanceSummary(sheet: String, studentId: String): Flow<ResultState<AttendanceSummary>>
    suspend fun getTodayRoutines(regYr: String, dept: String, semester: String, today: String): List<ClassRoutine>
    suspend fun isTodayHoliday(): Boolean
}