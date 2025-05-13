package com.sujoy.pbc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sujoy.pbc.data.repo.RepoImpl
import com.sujoy.pbc.domain.usecase.RoutineAlertUseCase
import com.sujoy.pbc.presentation.Util.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
                val prefs = Prefs(context)
                val regYear = prefs.getRegYear()
                val dept = prefs.getDepartment()
                val semester = prefs.getSemester()

                if (!regYear.isNullOrEmpty() && !dept.isNullOrEmpty() && !semester.isNullOrEmpty()) {
                    val routineUseCase = RoutineAlertUseCase(RepoImpl.getInstance(context))
                    CoroutineScope(Dispatchers.IO).launch {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            routineUseCase.execute(regYear, dept, semester, context)
                        }
                    }
                }
            }
        }
    }
}