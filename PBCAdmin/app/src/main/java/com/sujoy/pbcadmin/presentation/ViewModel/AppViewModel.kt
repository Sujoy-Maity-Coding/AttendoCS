package com.sujoy.pbcadmin.presentation.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.UseCase.AddRoutineDataUseCase
import com.sujoy.pbcadmin.domain.UseCase.AddSemesterWebUrlUseCase
import com.sujoy.pbcadmin.domain.UseCase.SaveRoutineDataUseCase
import com.sujoy.pbcadmin.domain.UseCase.UploadImageRoutineUseCase
import com.sujoy.pbcadmin.domain.model.ClassRoutine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val addSemesterWebUrlUseCase: AddSemesterWebUrlUseCase,
    private val uploadImageRoutineUseCase: UploadImageRoutineUseCase,
    private val saveRoutineDataUseCase: SaveRoutineDataUseCase,
    private val addRoutineDataUseCase: AddRoutineDataUseCase
    ) : ViewModel() {

    private val _addSemesterWebUrlState: MutableStateFlow<addSemesterWebUrlState> =
        MutableStateFlow(addSemesterWebUrlState())
    val addSemesterWebUrlState = _addSemesterWebUrlState.asStateFlow()

    private val _addRoutineState: MutableStateFlow<addRoutineState> =
        MutableStateFlow(addRoutineState())
    val addRoutineState = _addRoutineState.asStateFlow()

    private val _addRoutineDataState: MutableStateFlow<addRoutineState> = MutableStateFlow(addRoutineState())
    val addRoutineDataState = _addRoutineDataState.asStateFlow()

    fun addSemesterWebUrl(year: String, department: String, semester: String, webUrl: String) {
        viewModelScope.launch {
            addSemesterWebUrlUseCase.addSemesterWebUrl(year, department, semester, webUrl).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addSemesterWebUrlState.value = addSemesterWebUrlState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _addSemesterWebUrlState.value =
                            addSemesterWebUrlState(message = it.data)
                    }

                    is ResultState.Error -> {
                        _addSemesterWebUrlState.value =
                            addSemesterWebUrlState(isError = it.message)
                    }
                }
            }
        }
    }

    fun uploadRoutineImage(regYear: String, dept: String, sem: String, imageUri: Uri) {
        viewModelScope.launch {
            if (imageUri != null) {
                uploadImageRoutineUseCase.uploadRoutineImage(regYear, dept, sem, imageUri).collect {
                    when (it) {
                        is ResultState.Loading -> {
                            _addRoutineState.value = addRoutineState(isLoading = true)
                        }

                        is ResultState.Success -> {
                            _addRoutineState.value = addRoutineState(message = it.data)
                            saveRoutineData(regYear, dept, sem, it.data)
                        }

                        is ResultState.Error -> {
                            _addRoutineState.value = addRoutineState(isError = it.message)
                        }
                    }
                }
            }
            else{
                _addRoutineState.value = addRoutineState(isError = "Image not selected")
            }
        }
    }
    fun saveRoutineData(regYear: String, dept: String, sem: String, imageUrl: String) {
        viewModelScope.launch {
            saveRoutineDataUseCase.saveRoutineData(regYear, dept, sem, imageUrl).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addRoutineState.value = addRoutineState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _addRoutineState.value = addRoutineState(message = it.data)
                    }

                    is ResultState.Error -> {
                        _addRoutineState.value = addRoutineState(isError = it.message)
                    }
                }
            }
        }
    }

    fun addRoutineData(regYear: String, dept: String, sem: String, routine: ClassRoutine) {
        viewModelScope.launch {
            addRoutineDataUseCase.addRoutineData(regYear, dept, sem, routine).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addRoutineDataState.value = addRoutineState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _addRoutineDataState.value = addRoutineState(message = it.data)
                    }
                    is ResultState.Error -> {
                        _addRoutineDataState.value = addRoutineState(isError = it.message)
                    }
                }
            }
        }
    }
}
data class addSemesterWebUrlState(
    val isLoading: Boolean = false,
    val isError: String = "",
    val message: String = ""
)
data class addRoutineState(
    val isLoading: Boolean = false,
    val isError: String = "",
    val message: String = ""
)