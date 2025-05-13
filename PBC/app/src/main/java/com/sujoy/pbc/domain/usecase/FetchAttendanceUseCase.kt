package com.sujoy.pbc.domain.usecase

import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.AttendanceSummary
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAttendanceUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getAttendanceSummary(sheet: String, studentId: String): Flow<ResultState<AttendanceSummary>> =
        repo.getAttendanceSummary(sheet = sheet, studentId = studentId)
}