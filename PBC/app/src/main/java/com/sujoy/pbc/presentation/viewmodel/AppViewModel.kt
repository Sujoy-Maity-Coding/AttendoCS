package com.sujoy.pbc.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sujoy.pbc.common.ResultState
import com.sujoy.pbc.domain.model.AttendanceSummary
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.domain.model.UserDataParent
import com.sujoy.pbc.domain.usecase.CreateUserUseCase
import com.sujoy.pbc.domain.usecase.FetchAttendanceUseCase
import com.sujoy.pbc.domain.usecase.GetRoutineUseCase
import com.sujoy.pbc.domain.usecase.GetUserByIdUseCase
import com.sujoy.pbc.domain.usecase.GetWebUrlUseCase
import com.sujoy.pbc.domain.usecase.RoutineAlertUseCase
import com.sujoy.pbc.domain.usecase.SignInUserUseCase
import com.sujoy.pbc.domain.usecase.UpdateUserUseCase
import com.sujoy.pbc.domain.usecase.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val signInUserUseCase: SignInUserUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getRoutineUseCase: GetRoutineUseCase,
    private val getWebUrlUseCase: GetWebUrlUseCase,
    private val fetchAttendanceUseCase: FetchAttendanceUseCase,
    private val routineAlertUseCase: RoutineAlertUseCase
):ViewModel() {
    private val _signUpScreenState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState = _signUpScreenState.asStateFlow()

    private val _signInScreenState = MutableStateFlow(SignUpScreenState())
    val signInScreenState = _signInScreenState.asStateFlow()

    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()

    private val _updateProfileScreenState = MutableStateFlow(UpdateProfileScreenState())
    val updateProfileScreenState = _updateProfileScreenState.asStateFlow()

    private val _getRoutineScreenState = MutableStateFlow(GetRoutineScreenState())
    val getRoutineScreenState = _getRoutineScreenState.asStateFlow()

    private val _getWebUrlState = MutableStateFlow(GetWebUrlState())
    val getWebUrlState = _getWebUrlState.asStateFlow()

    private val _fetchAttendanceState = MutableStateFlow(FetchAttendanceState())
    val fetchAttendanceState = _fetchAttendanceState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAndScheduleAlerts(regYr: String, dept: String, semester: String, context: Context) {
        viewModelScope.launch {
            routineAlertUseCase.execute(regYr, dept, semester, context)
        }
    }

    fun createUser(userData: UserData, year: String, department: String, imageUri: Uri?, idNo:String) {
        viewModelScope.launch {
            if (imageUri != null) {
                uploadImageUseCase.uploadImage(imageUri, idNo).collect { imageResult ->
                    when (imageResult) {
                        is ResultState.Success -> {
                            val updatedUserData = userData.copy(image = imageResult.data)
                            registerUser(updatedUserData, year, department)
                        }
                        is ResultState.Error -> {
                            _signUpScreenState.value = SignUpScreenState(error = imageResult.message)
                        }
                        ResultState.Loading -> {
                            _signUpScreenState.value = SignUpScreenState(isLoading = true)
                        }
                    }
                }
            } else {
                registerUser(userData, year, department)
            }
        }
    }

    private fun registerUser(userData: UserData, year: String, department: String) {
        viewModelScope.launch {
            createUserUseCase.createUser(userData, year, department).collect {
                when (it) {
                    is ResultState.Error -> {
                        _signUpScreenState.value = SignUpScreenState(error = it.message)
                    }

                    ResultState.Loading -> {
                        _signUpScreenState.value = SignUpScreenState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _signUpScreenState.value = SignUpScreenState(userData = it.data)
                    }
                }
            }
        }
    }

    fun signInUser(userData: UserData) {
        viewModelScope.launch {
            signInUserUseCase.signInUser(userData).collect {
                when (it) {
                    is ResultState.Error -> {
                        _signInScreenState.value =
                            _signInScreenState.value.copy(isLoading = false, error = it.message)
                    }

                    ResultState.Loading -> {
                        _signInScreenState.value = _signInScreenState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _signInScreenState.value =
                            _signInScreenState.value.copy(isLoading = false, userData = it.data)
                    }
                }
            }
        }
    }

    fun getUserByUId(uId: String,regYr:String,dept:String){
        viewModelScope.launch {
            getUserByIdUseCase.getUserByUId(uId,regYr,dept).collectLatest {
                when(it){
                    is ResultState.Error -> {
                        _profileScreenState.value = _profileScreenState.value.copy(isLoading = false, error = it.message)
                    }
                    ResultState.Loading -> {
                        _profileScreenState.value = _profileScreenState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _profileScreenState.value = _profileScreenState.value.copy(isLoading = false, userData = it.data)
                    }
                }
            }
        }
    }

    fun updateUserData(userDataParent: UserDataParent,regYr:String,dept:String, imageUri: Uri?) {
        viewModelScope.launch {
            if (imageUri != null) {
                uploadImageUseCase.uploadImage(imageUri, userDataParent.userData!!.idNo)
                    .collect { imageResult ->
                        when (imageResult) {
                            is ResultState.Success -> {
                                val updatedUserData =
                                    userDataParent.userData?.copy(image = imageResult.data)
                                updateUser(
                                    userDataParent.copy(userData = updatedUserData),
                                    regYr,
                                    dept
                                )
                            }

                            is ResultState.Error -> {
                                _updateProfileScreenState.value =
                                    _updateProfileScreenState.value.copy(
                                        isLoading = false,
                                        error = imageResult.message
                                    )
                            }

                            ResultState.Loading -> {
                                _updateProfileScreenState.value =
                                    _updateProfileScreenState.value.copy(isLoading = true)
                            }
                        }
                    }
            }else{
                updateUser(
                    userDataParent.copy(userData = userDataParent.userData),
                    regYr,
                    dept
                )
            }
        }
    }
    fun updateUser(userDataParent: UserDataParent,regYr:String,dept:String) {
        viewModelScope.launch {
            updateUserUseCase.updateUserData(userDataParent, regYr, dept).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _updateProfileScreenState.value = _updateProfileScreenState.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }

                    ResultState.Loading -> {
                        _updateProfileScreenState.value =
                            _updateProfileScreenState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _updateProfileScreenState.value =
                            _updateProfileScreenState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                    }
                }
            }
        }
    }

    fun getRoutineImageUrl(regYear: String, dept: String, sem: String) {
        viewModelScope.launch {
            getRoutineUseCase.getRoutineImageUrl(regYear, dept, sem).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _getRoutineScreenState.value =
                            _getRoutineScreenState.value.copy(isLoading = false, error = it.message)
                    }
                    ResultState.Loading -> {
                        _getRoutineScreenState.value =
                            _getRoutineScreenState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getRoutineScreenState.value =
                            _getRoutineScreenState.value.copy(isLoading = false, imageUrl = it.data)
                    }
                }
            }
        }
    }
    fun getWebUrl(regYr: String, department: String, semester: String) {
        viewModelScope.launch {
            getWebUrlUseCase.getWebUrl(regYr,department,semester).collectLatest {
                when (it) {
                    is ResultState.Success -> {
                        _getWebUrlState.value = GetWebUrlState(webUrl = it.data)
                    }
                    is ResultState.Error -> {
                        _getWebUrlState.value = GetWebUrlState(error = it.message)
                    }
                    is ResultState.Loading -> {
                        _getWebUrlState.value = GetWebUrlState(isLoading = true)
                    }
                }
            }
        }
    }

    fun fetchAttendanceSummary(sheet: String, studentId: String) {
        viewModelScope.launch {
            fetchAttendanceUseCase.getAttendanceSummary(sheet, studentId).collectLatest {
                when (it) {
                    is ResultState.Success -> {
                        _fetchAttendanceState.value =
                            FetchAttendanceState(attendanceSummary = it.data)
                    }

                    is ResultState.Error -> {
                        _fetchAttendanceState.value = FetchAttendanceState(error = it.message)
                    }

                    is ResultState.Loading -> {
                        _fetchAttendanceState.value = FetchAttendanceState(isLoading = true)
                    }
                }
            }
        }
    }
}

data class SignUpScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: String? = null
)

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: UserDataParent? = null
)

data class UpdateProfileScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: UserDataParent? = null
)

data class GetRoutineScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val imageUrl: String? = null
)
data class GetWebUrlState(
    val webUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
data class FetchAttendanceState(
    val attendanceSummary: AttendanceSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)