package com.sujoy.pbcadmin.data.repo

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.model.ClassRoutine
import com.sujoy.pbcadmin.domain.repo.Repo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : Repo{
    override fun addSemesterWebUrl(year: String, department: String, semester: String, webUrl: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val semesterRef = firebaseFirestore.collection("Admin")
            .document(year)
            .collection("Departments")
            .document(department)
            .collection("Semesters")
        semesterRef.document(semester).set(mapOf("webUrl" to webUrl)).addOnSuccessListener {
            trySend(ResultState.Success("Web URL in Semester added successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose { close() }
    }

    override suspend fun uploadRoutineImage(
        regYear: String,
        dept: String,
        sem: String,
        imageUri: Uri
    ): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val ref = firebaseStorage.reference.child("routines/$regYear/$dept/$sem/routine.jpg")
        ref.putFile(imageUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                trySend(ResultState.Success(it.toString()))
            }
            }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose { close() }
    }

    override suspend fun saveRoutineData(
        regYear: String,
        dept: String,
        sem: String,
        imageUrl: String
    ): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val data = mapOf("routine_image" to imageUrl)
        firebaseFirestore.collection("routine")
            .document(regYear)
            .collection(dept)
            .document(sem)
            .set(data).addOnSuccessListener {
                trySend(ResultState.Success("Routine data saved successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose { close() }
    }

    override fun addRoutineData(
        regYear: String,
        dept: String,
        sem: String,
        routine: ClassRoutine
    ): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        firebaseFirestore.collection("class_routine").document(regYear).collection(dept).document(sem).collection("routine_data").add(routine).addOnSuccessListener {
            trySend(ResultState.Success("Routine data added successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose { close() }
    }
}