package com.sujoy.pbccs.Data.api

import com.sujoy.pbccs.domain.model.AttendanceSummary
import com.sujoy.pbccs.domain.model.Student
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AttendanceApi {

    @GET("exec")
    suspend fun getStudents(
        @Query("action") action: String = "getStudents",
        @Query("sheet") sheet: String,
    ): Response<List<Student>>

    @GET("exec")
    suspend fun updateAttendance(
        @Query("action") action: String = "updateAttendance",
        @Query("sheet") sheet: String,
        @Query("date") date: String,
        @Query("studentId") studentId: String,
        @Query("status") status: String
    ): Response<Unit>

    @GET("exec")
    suspend fun getAttendanceSummary(
        @Query("action") action: String = "getAttendanceSummary",
        @Query("sheet") sheet: String,
        @Query("studentId") studentId: String
    ): Response<AttendanceSummary>
}
