package com.sujoy.pbcadmin.domain.model

data class ClassRoutine(
    val teacherName: String= "",
    val subject: String = "",
    val day: String = "",         // e.g., "Monday"
    val startTime: String = "",   // e.g., "10:00"
    val endTime: String = ""      // e.g., "11:00"
)