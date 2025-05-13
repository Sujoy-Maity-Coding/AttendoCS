package com.sujoy.pbccs.domain.UseCase

import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.model.AttendanceSummary
import com.sujoy.pbccs.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAttendaceUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getAttendanceSummary(sheet: String, studentId: String): Flow<ResultState<AttendanceSummary>> =
        repo.getAttendanceSummary(sheet = sheet, studentId = studentId)
}