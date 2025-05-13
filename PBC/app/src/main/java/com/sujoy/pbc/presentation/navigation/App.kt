package com.sujoy.pbc.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.sujoy.pbc.presentation.screens.AttendanceUI
import com.sujoy.pbc.presentation.screens.HomeUI
import com.sujoy.pbc.presentation.screens.LoginScreen
import com.sujoy.pbc.presentation.screens.ProfileUI
import com.sujoy.pbc.presentation.screens.RoutineUI
import com.sujoy.pbc.presentation.screens.SignUpScreen
import com.sujoy.pbc.presentation.screens.UpdateUI

@Composable
fun App(modifier: Modifier = Modifier, firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    Box(modifier = modifier.fillMaxSize()) {
        var startScreen = if (firebaseAuth.currentUser == null) {
            SubNavigation.LoginSignUpScreen
        } else {
            SubNavigation.MainHomeScreen
        }
        NavHost(navController = navController, startDestination = startScreen) {
            navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.signUpScreen) {
                composable<Routes.signUpScreen> {
                    SignUpScreen(navController=navController)
                }
                composable<Routes.LoginScreen> {
                    LoginScreen(navController=navController)
                }
            }
            navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.homeScreen) {
                composable<Routes.homeScreen> {
                    HomeUI(firebaseAuth = firebaseAuth, navController = navController)
                }
                composable<Routes.profileScreen> {
                    ProfileUI(firebaseAuth = firebaseAuth, navController = navController)
                }
                composable<Routes.routineScreen> {
                    val args=it.toRoute<Routes.routineScreen>()
                    RoutineUI(args=args)
                }
                composable<Routes.attendanceScreen> {
                    val args=it.toRoute<Routes.attendanceScreen>()
                    AttendanceUI(args=args)
                }
                composable<Routes.updateScreen> {
                    UpdateUI(firebaseAuth = firebaseAuth, navController = navController)
                }
            }
        }
    }
}