package com.sujoy.pbccs.Data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.Data.api.AttendanceApi
import com.sujoy.pbccs.Data.api.RetrofitProvider
import com.sujoy.pbccs.domain.model.AttendanceSummary
import com.sujoy.pbccs.domain.model.Student
import com.sujoy.pbccs.domain.model.StudentData
import com.sujoy.pbccs.domain.repo.Repo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class RepoImpl @Inject constructor(private val firestore: FirebaseFirestore) : Repo {

    private val api: AttendanceApi
        get() = RetrofitProvider.getApi()

    override suspend fun getStudents(sheet: String): Flow<ResultState<List<Student>>> = callbackFlow {
        trySend(ResultState.Loading)
        val response = api.getStudents(sheet = sheet)
        if (response.isSuccessful) {
            response.body()?.let {
                trySend(ResultState.Success(it))
            } ?: trySend(ResultState.Error("Response body is null"))
        } else {
            trySend(ResultState.Error("Request failed with code: ${response.code()}"))
        }
        awaitClose { close() }
    }

    override suspend fun markAttendance(sheet: String, date: String, studentId: String, status: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val response = api.updateAttendance(sheet = sheet, date = date, studentId = studentId, status = status)
        if (response.isSuccessful) {
            trySend(ResultState.Success("Attendance marked successfully"))
        } else {
            trySend(ResultState.Error("Request failed with code: ${response.code()}"))
        }
        awaitClose { close() }
    }

    override suspend fun getStudentById(regYr: String, department: String, idNo: String): Flow<ResultState<StudentData?>> = callbackFlow {
        trySend(ResultState.Loading)

        firestore.collection("User").document(regYr).collection(department)
            .whereEqualTo("idNo", idNo) // Ensure idNo type matches Firestore
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val documents = it.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        val studentData = documents[0].toObject(StudentData::class.java)
                        trySend(ResultState.Success(studentData))
                    }
                    else {
                        trySend(ResultState.Success(null))
                    }
                } else {
                    trySend(ResultState.Error(it.exception?.message ?: "Unknown error"))
                }
            }

        awaitClose { close() }
    }

    override suspend fun getWebUrl(regYr: String, department: String, semester: String): Flow<ResultState<String?>> = callbackFlow{
        trySend(ResultState.Loading)
        try {
            val document = firestore
                .collection("Admin")
                .document(regYr)
                .collection("Departments")
                .document(department)
                .collection("Semesters")
                .document(semester)
                .get()
                .await()

            val webUrl = document.getString("webUrl")
            trySend(ResultState.Success(webUrl))
        } catch (e: Exception) {
            trySend(ResultState.Error(e.localizedMessage ?: "Unknown Error"))
        }
        awaitClose { close() }
    }

    override suspend fun getAttendanceSummary(sheet: String, studentId: String): Flow<ResultState<AttendanceSummary>> = callbackFlow{
        try {
            trySend(ResultState.Loading)

            val response: Response<AttendanceSummary> = api.getAttendanceSummary(sheet = sheet, studentId = studentId)

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("RepoImpl", "Response Body: $body")
                if (body != null) {
                    trySend(ResultState.Success(body))
                } else {
                    trySend(ResultState.Error("Response body is null"))
                }
            } else {
                trySend(ResultState.Error("Request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            trySend(ResultState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}"))
        }
        awaitClose { close() }
    }

}