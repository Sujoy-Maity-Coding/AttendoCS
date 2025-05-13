package com.sujoy.pbc.data.api

import com.sujoy.pbc.domain.model.AttendanceSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AttendanceApi {
    @GET("exec")
    suspend fun getAttendanceSummary(
        @Query("action") action: String = "getAttendanceSummary",
        @Query("sheet") sheet: String,
        @Query("studentId") studentId: String
    ): Response<AttendanceSummary>
}