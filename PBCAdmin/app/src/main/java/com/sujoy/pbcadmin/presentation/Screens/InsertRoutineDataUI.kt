package com.sujoy.pbcadmin.presentation.Screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sujoy.pbcadmin.R
import com.sujoy.pbcadmin.domain.model.ClassRoutine
import com.sujoy.pbcadmin.presentation.Utils.DropdownMenuComponent
import com.sujoy.pbcadmin.presentation.ViewModel.AppViewModel

@Composable
fun InsertRoutineDataUI(modifier: Modifier = Modifier, viewModel: AppViewModel = hiltViewModel()) {
    var context = LocalContext.current
    var reg_yr by remember { mutableStateOf("") }
    var dept by remember { mutableStateOf("") }
    var sem by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val routineDataState = viewModel.addRoutineDataState.collectAsState()

    LaunchedEffect(routineDataState.value) {
        if (routineDataState.value.message.isNotEmpty()) {
            Toast.makeText(context, routineDataState.value.message, Toast.LENGTH_SHORT).show()
            showDialog = true // Set showDialog after showing the Toast
            // Optional: reset form
            teacherName = ""
            subject = ""
            day = ""
            startTime = ""
            endTime = ""
        } else if (routineDataState.value.isError.isNotEmpty()) {
            Toast.makeText(context, routineDataState.value.isError, Toast.LENGTH_SHORT).show()
        } else if (routineDataState.value.isLoading) {
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
                text = "Routine Data Insert",
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
            reg_yr = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuComponent(
            "Select Department",
            listOf("COSH", "PHSE", "BNG", "ENG")
        ) { dept = it }
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuComponent(
            "Select Semester",
            listOf("1", "2", "3", "4", "5", "6", "7", "8")
        ) {
            sem = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = teacherName,
            onValueChange = { teacherName = it },
            label = { Text("Teacher Name") },
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
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject Name") },
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
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = day,
            onValueChange = { day = it },
            label = { Text("Enter Day") },
            placeholder = { Text(text = "e.g., Monday") },
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
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Class Start Time") },
            placeholder = { Text(text = "e.g., 10:00") },
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
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("Class End Time") },
            placeholder = { Text(text = "e.g., 11:00") },
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
                if (reg_yr.isNotEmpty() && dept.isNotEmpty() && sem.isNotEmpty() && teacherName.isNotEmpty() && subject.isNotEmpty() && day.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                    viewModel.addRoutineData(
                        regYear = reg_yr, dept = dept, sem = sem, routine = ClassRoutine(
                            teacherName = teacherName,
                            subject = subject,
                            day = day,
                            startTime = startTime,
                            endTime = endTime
                        )
                    )
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
                "Insert Routine",
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
                    text = "Congrats insert routine successfully 🎉",
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