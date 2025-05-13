package com.sujoy.pbc.domain.usecase

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sujoy.pbc.domain.repo.Repo
import com.sujoy.pbc.presentation.Util.NotificationScheduler
import java.time.LocalDate
import javax.inject.Inject

class RoutineAlertUseCase @Inject constructor(private val repo: Repo) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun execute(regYr: String, dept: String, semester: String, context: Context) {
        if (repo.isTodayHoliday()) return

        val today = LocalDate.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val routines = repo.getTodayRoutines(regYr, dept, semester, today)

        routines.forEach {
            NotificationScheduler.scheduleClassNotification(context, it.startTime, it.subject)
        }
    }
}