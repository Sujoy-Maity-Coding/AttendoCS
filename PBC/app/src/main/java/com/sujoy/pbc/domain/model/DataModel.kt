package com.sujoy.pbc.domain.model

data class UserData (
    var Uid : String="",
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
data class UserDataParent(val nodeId:String, val userData: UserData?)

data class AttendanceSummary(
    val id: String,
    val name: String,
    val totalAttendance: Int,
    val totalClass: Int,
    val attendancePercentage: Double
)

data class ClassRoutine(
    val teacherName: String= "",
    val subject: String = "",
    val day: String = "",         // e.g., "Monday"
    val startTime: String = "",   // e.g., "10:00"
    val endTime: String = ""      // e.g., "11:00"
)