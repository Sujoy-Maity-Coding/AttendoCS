package com.sujoy.pbc

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.navigation.App
import com.sujoy.pbc.presentation.viewmodel.AppViewModel
import com.sujoy.pbc.ui.theme.PBCTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: AppViewModel by viewModels() // Hilt ViewModel works here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prefs = Prefs(applicationContext)
            val regYear = prefs.getRegYear()
            val dept = prefs.getDepartment()
            val semester = prefs.getSemester()

            if (!regYear.isNullOrEmpty() && !dept.isNullOrEmpty() && !semester.isNullOrEmpty()) {
                viewModel.checkAndScheduleAlerts(
                    regYr = regYear,
                    dept = dept,
                    semester = semester,
                    context = applicationContext
                )
            }
        }

        enableEdgeToEdge()

        setContent {
            PBCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        App(firebaseAuth = firebaseAuth)
                    }
                }
            }
        }
    }
}
