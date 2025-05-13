package com.sujoy.pbcadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sujoy.pbcadmin.presentation.Navigation.App
import com.sujoy.pbcadmin.presentation.Screens.InsertRoutineUI
import com.sujoy.pbcadmin.presentation.ViewModel.AppViewModel
import com.sujoy.pbcadmin.ui.theme.PBCAdminTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PBCAdminTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: AppViewModel = hiltViewModel()
                    App(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}