package com.sujoy.pbccs.domain.repo

import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.model.AttendanceSummary
import com.sujoy.pbccs.domain.model.Student
import com.sujoy.pbccs.domain.model.StudentData
import kotlinx.coroutines.flow.Flow

interface Repo{
    suspend fun getStudents(sheet: String): Flow<ResultState<List<Student>>>
    suspend fun markAttendance(sheet: String, date: String, studentId: String, status: String): Flow<ResultState<String>>
    suspend fun getStudentById(regYr: String, department: String, idNo: String): Flow<ResultState<StudentData?>>
    suspend fun getWebUrl(regYr: String, department: String, semester: String): Flow<ResultState<String?>>
    suspend fun getAttendanceSummary(sheet: String, studentId: String): Flow<ResultState<AttendanceSummary>>
}
