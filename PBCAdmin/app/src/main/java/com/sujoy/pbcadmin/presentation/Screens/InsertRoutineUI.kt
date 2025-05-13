package com.sujoy.pbcadmin.presentation.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.sujoy.pbcadmin.R
import com.sujoy.pbcadmin.presentation.Utils.DropdownMenuComponent
import com.sujoy.pbcadmin.presentation.ViewModel.AppViewModel

@Composable
fun InsertRoutineUI(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var year by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val saveRoutineState = viewModel.addRoutineState.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    LaunchedEffect(saveRoutineState.value) {
        if (saveRoutineState.value.message.isNotEmpty()) {
            Toast.makeText(context, saveRoutineState.value.message, Toast.LENGTH_SHORT).show()
            showDialog = true // Set showDialog after showing the Toast
            imageUri = null
        }
        else if (saveRoutineState.value.isError.isNotEmpty()) {
            Toast.makeText(context, saveRoutineState.value.isError, Toast.LENGTH_SHORT).show()
        }
        else if (saveRoutineState.value.isLoading) {
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
                    text = "Routine Insert",
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
            // Image Picker
            if (imageUri == null) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color.Cyan, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { launcher.launch("image/*") }) {
                    Text(
                        text = "Select\nRoutine mage",
                        fontSize = 17.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "routine img",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(250.dp)
                        .border(1.dp, Color.Cyan, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.uploadRoutineImage(year, department, semester, imageUri!!)
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
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            Button(
                onClick = {
                    showDialog = false
                },
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Cyan)
            ) {
                Text(text = "Back", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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