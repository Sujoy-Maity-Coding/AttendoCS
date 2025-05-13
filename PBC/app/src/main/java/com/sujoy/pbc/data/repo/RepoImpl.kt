package com.sujoy.pbc.data.repo

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.data.api.AttendanceApi
import com.sujoy.pbc.data.api.RetrofitProvider
import com.sujoy.pbc.domain.model.AttendanceSummary
import com.sujoy.pbc.domain.model.ClassRoutine
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.domain.model.UserDataParent
import com.sujoy.pbc.domain.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : Repo {
    private val api: AttendanceApi
        get() = RetrofitProvider.getApi()

    companion object {
        fun getInstance(context: Context): RepoImpl {
            return RepoImpl(
                FirebaseAuth.getInstance(),
                FirebaseFirestore.getInstance(),
                FirebaseStorage.getInstance()
            )
        }
    }

    override fun registerUserWithEmailPassword(userData: UserData, year:String, department:String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        val userId = authResult.result.user?.uid ?: ""
                        val updatedUserData = userData.copy(Uid = userId)

                        firebaseFirestore.collection("User").document(year)
                            .collection(department)
                            .document(userId).set(updatedUserData)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    trySend(ResultState.Success("User Registered Successfully"))
                                } else {
                                    trySend(ResultState.Error(task.exception?.localizedMessage.toString()))
                                }
                            }
                    } else {
                        trySend(ResultState.Error(authResult.exception?.localizedMessage.toString()))
                    }
                }
            awaitClose { close() }
        }

    override fun signInWithEmailPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User Sign In Successfully"))
                    } else {
                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                    }
                }
            awaitClose {
                close()
            }
        }
    override fun uploadImageToFirebase(imageUri: Uri, userId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add unique suffix to prevent overwriting
        val uniqueFileName = "${userId}_${System.currentTimeMillis()}.jpg"
        val storageRef = firebaseStorage.reference.child("user_images/$uniqueFileName")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    trySend(ResultState.Success(downloadUri.toString()))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Image upload failed"))
            }

        awaitClose { close() }
    }
    override fun getUserByUId(uId: String,regYr:String,dept:String): Flow<ResultState<UserDataParent>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection("User").document(regYr).collection(dept).document(uId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result.toObject(UserData::class.java)
                val userDataParent = UserDataParent(nodeId = it.result.id, userData = data)
                if (data != null) {
                    trySend(ResultState.Success(userDataParent))
                } else {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            } else {
                trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
            }
        }
        awaitClose {
            close()
        }
    }

    override fun updateUserData(userDataParent: UserDataParent,regYr:String,dept:String): Flow<ResultState<UserDataParent>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection("User").document(regYr).collection(dept).document(userDataParent.nodeId)
                .set(userDataParent.userData!!).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(userDataParent))
                    } else {
                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                    }
                }
            awaitClose {
                close()
            }
        }

    override fun getRoutineImageUrl(
        regYear: String,
        dept: String,
        sem: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val docRef = firebaseFirestore
            .collection("routine")
            .document(regYear)
            .collection(dept)
            .document(sem)

        val listener = docRef.get().addOnSuccessListener { document ->
            val url = document.getString("routine_image")
            if (url != null) {
                trySend(ResultState.Success(url))
            } else {
                trySend(ResultState.Error("Routine image URL not found"))
            }
        }.addOnFailureListener { exception ->
            trySend(ResultState.Error(exception.localizedMessage ?: "Unknown error"))
        }

        awaitClose { close() }
    }

    override suspend fun getWebUrl(
        regYr: String,
        department: String,
        semester: String
    ): Flow<ResultState<String?>> = callbackFlow{
        trySend(ResultState.Loading)
        try {
            val document = firebaseFirestore
                .collection("Admin")
                .document(regYr)
                .collection("Departments")
                .document(department)
                .collection("Semesters")
                .document(semester)
                .get()
                .await()

            val webUrl = document.getString("webUrl")
            if (!webUrl.isNullOrEmpty()) {
                trySend(ResultState.Success(webUrl))
            } else {
                trySend(ResultState.Error("Web URL not found"))
            }
        } catch (e: Exception) {
            trySend(ResultState.Error(e.localizedMessage ?: "Unknown Error"))
        }
        awaitClose { close() }
    }

    override suspend fun getAttendanceSummary(
        sheet: String,
        studentId: String
    ): Flow<ResultState<AttendanceSummary>> = callbackFlow{
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
    private val apiKey = "AIzaSyC1QtHKG6C7oNZGVBxbjpyQz0KaleQbMXQ"
    private val calendarId = "en.indian%23holiday%40group.v.calendar.google.com"

    override suspend fun getTodayRoutines(regYr: String, dept: String, semester: String, today: String): List<ClassRoutine> {
        return try {
            val snapshot = firebaseFirestore
                .collection("class_routine")
                .document(regYr)
                .collection(dept)
                .document(semester)
                .collection("routine_data")
                .whereEqualTo("day", today)
                .get()
                .await()

            snapshot.toObjects(ClassRoutine::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun isTodayHoliday(): Boolean = withContext(Dispatchers.IO) {
        try {
            val today = LocalDate.now()
            val timeMin = "${today}T00:00:00Z"
            val timeMax = "${today}T23:59:59Z"

            val urlStr = "https://www.googleapis.com/calendar/v3/calendars/$calendarId/events" +
                    "?timeMin=$timeMin&timeMax=$timeMax&key=$apiKey"

            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(response)
            val items = json.getJSONArray("items")

            return@withContext items.length() > 0 // True = It's a holiday
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false // Fail-safe: not a holiday
        }
    }
}