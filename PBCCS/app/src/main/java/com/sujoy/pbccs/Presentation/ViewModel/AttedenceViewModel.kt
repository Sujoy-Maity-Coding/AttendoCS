package com.sujoy.pbccs.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sujoy.pbccs.Common.ResultState.ResultState
import com.sujoy.pbccs.domain.UseCase.FetchAttendaceUseCase
import com.sujoy.pbccs.domain.UseCase.FetchStudentsUseCase
import com.sujoy.pbccs.domain.UseCase.GetStudenyByIdUseCase
import com.sujoy.pbccs.domain.UseCase.GetWebUrlUseCase
import com.sujoy.pbccs.domain.UseCase.MarkAttendanceUseCase
import com.sujoy.pbccs.domain.model.AttendanceSummary
import com.sujoy.pbccs.domain.model.Student
import com.sujoy.pbccs.domain.model.StudentData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val fetchStudentsUseCase: FetchStudentsUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase,
    private val getStudentByIdUseCase: GetStudenyByIdUseCase,
    private val getWebUrlUseCase: GetWebUrlUseCase,
    private val fetchAttendanceUseCase: FetchAttendaceUseCase
) : ViewModel() {

    private val _fetchStudentState : MutableStateFlow<FetchStudentState> = MutableStateFlow(FetchStudentState())
    val fetchStudentState : StateFlow<FetchStudentState> = _fetchStudentState.asStateFlow()

    private val _markAttendanceState : MutableStateFlow<MarkAttendanceState> = MutableStateFlow(MarkAttendanceState())
    val markAttendanceState : StateFlow<MarkAttendanceState> = _markAttendanceState.asStateFlow()

    private val _getStudentByIdState : MutableStateFlow<GetStudentByIdState> = MutableStateFlow(GetStudentByIdState())
    val getStudentByIdState : StateFlow<GetStudentByIdState> = _getStudentByIdState.asStateFlow()

    private val _getWebUrlState : MutableStateFlow<GetWebUrlState> = MutableStateFlow(GetWebUrlState())
    val getWebUrlState : StateFlow<GetWebUrlState> = _getWebUrlState.asStateFlow()

    private val _fetchAttendanceState : MutableStateFlow<FetchAttendanceState> = MutableStateFlow(FetchAttendanceState())
    val fetchAttendanceState : StateFlow<FetchAttendanceState> = _fetchAttendanceState.asStateFlow()

    fun resetStudentByIdState() {
        _getStudentByIdState.value = GetStudentByIdState()
    }

    fun resetFetchAttendanceState() {
        _fetchAttendanceState.value = FetchAttendanceState()
    }

    fun fetchStudents(sheet: String) {
        viewModelScope.launch {
            fetchStudentsUseCase.getStudents(sheet = sheet).collectLatest {
                when (it) {
                    is ResultState.Success -> {
                        _fetchStudentState.value = FetchStudentState(students = it.data)
                    }

                    is ResultState.Error -> {
                        _fetchStudentState.value = FetchStudentState(error = it.message)
                    }

                    is ResultState.Loading -> {
                        _fetchStudentState.value = FetchStudentState(isLoading = true)
                    }
                }
            }
        }
    }

    fun markAttendance(sheet: String, date: String, studentId: String, status: String) {
        viewModelScope.launch {
            markAttendanceUseCase.markAttendance(sheet, date, studentId, status).collectLatest{
                when (it) {
                    is ResultState.Success -> {
                        _markAttendanceState.value = MarkAttendanceState(isAttendanceMarked = it.data)
                    }
                    is ResultState.Error -> {
                        _markAttendanceState.value = MarkAttendanceState(error = it.message)
                    }
                    is ResultState.Loading -> {
                        _markAttendanceState.value = MarkAttendanceState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getStudentById(regYr: String, department: String, idNo: String) {
        viewModelScope.launch {
            _getStudentByIdState.value = GetStudentByIdState(isLoading = true)

            getStudentByIdUseCase.getStudentById(regYr, department, idNo).collectLatest {
                _getStudentByIdState.value = when (it) {
                    is ResultState.Success -> GetStudentByIdState(studentData = it.data)
                    is ResultState.Error -> GetStudentByIdState(error = it.message)
                    is ResultState.Loading -> GetStudentByIdState(isLoading = true)
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

data class FetchStudentState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class MarkAttendanceState(
    val isAttendanceMarked: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
data class GetStudentByIdState(
    val studentData: StudentData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
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