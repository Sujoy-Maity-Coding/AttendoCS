package com.sujoy.pbc.presentation.Util

import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveRegYear(year: String) {
        prefs.edit().putString("reg_year", year).apply()
    }

    fun saveDepartment(dept: String) {
        prefs.edit().putString("department", dept).apply()
    }

    fun saveSemester(sem: String) {
        prefs.edit().putString("semester", sem).apply()
    }

    fun updateSemester(sem: String) {
        prefs.edit().putString("semester", sem).apply()
    }

    fun getRegYear(): String? = prefs.getString("reg_year", null)

    fun getDepartment(): String? = prefs.getString("department", null)

    fun getSemester(): String? = prefs.getString("semester", null)

    fun clearUserPrefs() {
        prefs.edit().remove("reg_year").remove("department").remove("semester").apply()
    }
}
