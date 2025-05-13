package com.sujoy.pbccs.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sujoy.pbccs.Presentation.Screens.AttendanceScreen
import com.sujoy.pbccs.Presentation.Screens.DepartmentUI
import com.sujoy.pbccs.Presentation.Screens.PaperUI
import com.sujoy.pbccs.Presentation.ViewModel.AttendanceViewModel

@Composable
fun App(modifier: Modifier = Modifier, viewModel: AttendanceViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Department) {
        composable<Routes.Department> { DepartmentUI(navController=navController, viewModel=viewModel) }
        composable<Routes.Paper> {
            val args=it.toRoute<Routes.Paper>()
            PaperUI(navController=navController, args=args)
        }
        composable<Routes.Attendance> {
            val args=it.toRoute<Routes.Attendance>()
            AttendanceScreen(viewModel=viewModel, navController=navController, args=args)
        }
    }
}