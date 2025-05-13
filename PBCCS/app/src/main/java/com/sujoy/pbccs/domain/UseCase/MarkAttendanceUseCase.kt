package com.sujoy.pbccs.domain.UseCase

import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(private val repo: Repo) {
    suspend fun markAttendance(sheet: String, date: String, studentId: String, status: String): Flow<ResultState<String>> =
        repo.markAttendance(sheet, date, studentId, status)
}