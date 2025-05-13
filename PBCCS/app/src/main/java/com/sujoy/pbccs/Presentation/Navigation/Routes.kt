package com.sujoy.pbccs.Presentation.Navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object Login : Routes()

    @Serializable
    object Register : Routes()

    @Serializable
    object Home : Routes()

    @Serializable
    object Department : Routes()

    @Serializable
    object Profile : Routes()

    @Serializable
    data class Paper(val regYr: String, val department: String, val semester: String) : Routes()

    @Serializable
    data class Attendance(val paper: String, val date: String, val regYr: String, val department: String) : Routes()
}