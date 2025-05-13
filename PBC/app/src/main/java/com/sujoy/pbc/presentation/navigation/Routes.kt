package com.sujoy.pbc.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{
    @Serializable
    object LoginSignUpScreen: SubNavigation()
    @Serializable
    object MainHomeScreen: SubNavigation()
}

sealed class Routes {
    @Serializable
    object LoginScreen : Routes()

    @Serializable
    object signUpScreen : Routes()

    @Serializable
    object profileScreen : Routes()

    @Serializable
    object homeScreen : Routes()

    @Serializable
    data class routineScreen(val sem:String) : Routes()

    @Serializable
    data class attendanceScreen(val id:String) : Routes()

    @Serializable
    object updateScreen : Routes()
}