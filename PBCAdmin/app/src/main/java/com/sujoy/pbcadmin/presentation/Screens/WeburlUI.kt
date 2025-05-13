package com.sujoy.pbcadmin.presentation.Screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sujoy.pbcadmin.R
import com.sujoy.pbcadmin.presentation.Utils.DropdownMenuComponent
import com.sujoy.pbcadmin.presentation.ViewModel.AppViewModel

@Composable
fun WeburlUI(viewModel: AppViewModel = hiltViewModel()) {
    val context = LocalContext.current

    var year by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    var semester by remember { mutableStateOf("") }
    var semesterUrl by remember { mutableStateOf("") }
    var semesterstate = viewModel.addSemesterWebUrlState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(semesterstate.value) {
        if (semesterstate.value.message.isNotEmpty()) {
            Toast.makeText(context, semesterstate.value.message, Toast.LENGTH_SHORT).show()
            showDialog = true // Set showDialog after showing the Toast
            // Optional: reset form
            semesterUrl = ""
        } else if (semesterstate.value.isError.isNotEmpty()) {
            Toast.makeText(context, semesterstate.value.isError, Toast.LENGTH_SHORT).show()
        } else if (semesterstate.value.isLoading) {
            Toast.makeText(context, "Loading..", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    Color.Cyan,
                    shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                )
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "WebUrl Insert",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Serif
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        DropdownMenuComponent(
            "Select registration Year",
            listOf("2023", "2024", "2025")
        ) {
            year = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuComponent(
            "Select Department",
            listOf("COSH", "PHSE", "BNG", "ENG")
        ) { department = it }
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuComponent(
            "Select Semester",
            listOf("1", "2", "3", "4", "5", "6", "7", "8")
        ) {
            semester = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = semesterUrl,
            onValueChange = { semesterUrl = it },
            label = { Text("Enter Semester Url.") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Cyan,
                unfocusedIndicatorColor = Color.Cyan,
                cursorColor = Color.Cyan,
                focusedLabelColor = Color.Cyan,
                unfocusedLabelColor = Color.Cyan
            ),
            textStyle = TextStyle(
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (year.isNotEmpty() && department.isNotEmpty() &&
                    semester.isNotEmpty() && semesterUrl.isNotEmpty()
                ) {
                    viewModel.addSemesterWebUrl(year, department, semester, semesterUrl)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Cyan),
            border = BorderStroke(2.dp, Color.Cyan)
        ) {
            Text(
                "Insert Url",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
    // ✅ Success AlertDialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color.Cyan)
                ) {
                    Text(
                        text = "Back",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            },
            icon = {
                Image(
                    painterResource(id = R.drawable.success),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
            },
            text = {
                Text(
                    text = "Congrats insert WebUrl on semester successfully 🎉",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            },
            containerColor = Color.White,
            textContentColor = Color.Black,
            shape = RoundedCornerShape(20.dp),
        )
    }
}