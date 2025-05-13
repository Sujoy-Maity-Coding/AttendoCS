package com.sujoy.pbccs.Presentation.Screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sujoy.pbccs.Common.Constant.Constant
import com.sujoy.pbccs.Data.api.RetrofitProvider
import com.sujoy.pbccs.Presentation.Navigation.Routes
import com.sujoy.pbccs.Presentation.Util.DropdownMenuComponent
import com.sujoy.pbccs.Presentation.ViewModel.AttendanceViewModel
import com.sujoy.pbccs.R

@Composable
fun DepartmentUI(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AttendanceViewModel,
    ) {
    var selectedYear by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("") }
    var selectedSemester by remember { mutableStateOf("") }
    val fetchWebUrlState by viewModel.getWebUrlState.collectAsState()
    val context= LocalContext.current

    LaunchedEffect(fetchWebUrlState) {
        fetchWebUrlState.webUrl?.takeIf { it.isNotEmpty() }?.let {
            Constant.webUrl=it
            RetrofitProvider.init(it) // Initialize dynamically
            Toast.makeText(context, "WebUrl fetched successfully😊", Toast.LENGTH_SHORT).show()
            navController.navigate(
                Routes.Paper(
                    regYr = selectedYear,
                    department = selectedDepartment,
                    semester = selectedSemester
                )
            )
        }
        fetchWebUrlState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        fetchWebUrlState.isLoading.takeIf { it }?.let {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_pbc),
            contentDescription = "bg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f),
            contentAlignment = Alignment.Center) {
            Text(
                text = "Fetch WebUrl",
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E8AEC),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Dear Teacher, select the Student's Registration Year, Department, and Semester to fetch the exact Google Sheet database and manage attendance seamlessly.",
                fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(15.dp))
            DropdownMenuComponent("Select registration Year", listOf("2023", "2024", "2025")) {
                selectedYear = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            DropdownMenuComponent(
                "Select Department",
                listOf("COSH", "PHSE", "BNG", "ENG")
            ) { selectedDepartment = it }
            Spacer(modifier = Modifier.height(10.dp))
            DropdownMenuComponent("Select Semester", listOf("1", "2", "3", "4", "5", "6", "7", "8")) {
                selectedSemester = it
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                viewModel.getWebUrl(selectedYear, selectedDepartment, selectedSemester)
            },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                border = BorderStroke(2.dp, Color(0xFF03A9F4))
            ) {
                Text("Fetch WebUrl", fontSize = 20.sp, color = Color(0xFF03A9F4), fontWeight = FontWeight.Bold)
            }
        }
    }
}