package com.sujoy.pbc.presentation.Util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.sujoy.pbc.R
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Random
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleClassNotification(context: Context, time: String, subject: String) {
        val formatter = DateTimeFormatter.ofPattern("H:mm")
        val classTime = LocalTime.parse(time, formatter)

        // Today’s full datetime
        val now = LocalDateTime.now()

        // Class time today
        val classDateTime = LocalDateTime.of(LocalDate.now(), classTime)
        val notifyDateTime = classDateTime.minusMinutes(30)

        val delay = Duration.between(now, notifyDateTime).toMillis()

        if (delay > 0) {
            Log.d("NotificationScheduler", "Scheduling $subject at $notifyDateTime with delay $delay")

            val workRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("subject" to subject))
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        } else {
            Log.d("NotificationScheduler", "Skipped $subject. notifyTime=$notifyDateTime is in the past")
        }
    }

}

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("NotifyWorker", "doWork called with subject = ${inputData.getString("subject")}")
        val subject = inputData.getString("subject") ?: "Class"
        showNotification(applicationContext, "Upcoming Class", "Your $subject class starts in 30 mins")
        return Result.success()
    }

    private fun showNotification(context: Context, title: String, content: String) {
        val channelId = "class_alerts"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Class Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.audience) // make sure you have this icon
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notificationManager.notify(Random().nextInt(), builder.build())
        } else {
            Log.e("NotifyWorker", "NotificationManager is null")
        }
    }

}