package com.sujoy.pbccs.domain.model

import com.google.gson.annotations.SerializedName

data class Student(
    val id: String,
    val name: String
)

data class StudentData (
    val idNo: String="",
    var roll : String="",
    var Name : String="",
    var department : String="",
    var year : String="",
    var semester:String="",
    var phone : String="",
    var email : String="",
    val password : String="",
    var image : String="",
)

data class AttendanceSummary(
    val id: String,
    val name: String,
    val totalAttendance: Int,
    val totalClass: Int,
    val attendancePercentage: Double
)
