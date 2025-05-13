package com.sujoy.pbccs.Presentation.Screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.sujoy.pbccs.Presentation.Navigation.Routes
import com.sujoy.pbccs.R
import java.util.Calendar

@Composable
fun PaperUI(modifier: Modifier = Modifier, navController: NavHostController, args: Routes.Paper) {
    var subject by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_pbc),
            contentDescription = "bg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Fetch Student",
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
            Text(text = "Dear Teacher, place the attendance paper name and select the date to fetch student records from Google Sheets effortlessly!",
                fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it.uppercase() },
                label = { Text("Enter Paper") },
                placeholder = { Text(text = "e.g. CC1, CC2", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF03A9F4),
                    unfocusedIndicatorColor = Color(0xFF03A9F4),
                    cursorColor = Color(0xFF03A9F4),
                    focusedLabelColor = Color(0xFF03A9F4),
                    unfocusedLabelColor = Color(0xFF03A9F4)
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Enter Date (M/D/YYYY)") },
                placeholder = { Text(text = "e.g. 7/20/2024", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF03A9F4),
                    unfocusedIndicatorColor = Color(0xFF03A9F4),
                    cursorColor = Color(0xFF03A9F4),
                    focusedLabelColor = Color(0xFF03A9F4),
                    unfocusedLabelColor = Color(0xFF03A9F4)
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                val formattedDate = formatDate(date)
                if (formattedDate != null) {
                    navController.navigate(
                        Routes.Attendance(
                            regYr = args.regYr,
                            department = args.department,
                            paper = subject,
                            date = formattedDate
                        )
                    )
                } else {
                    Toast.makeText(context, "Invalid Date Format! Use M/D/YYYY", Toast.LENGTH_SHORT)
                        .show()
                }
            },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4))
            ) {
                Text("Fetch Student", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ✅ Function to format date correctly (No Leading Zeros)
fun formatDate(input: String): String? {
    val regex = Regex("""^\s*(0?[1-9]|1[0-2])\s*/\s*(0?[1-9]|[12]\d|3[01])\s*/\s*(\d{4})\s*$""")
    val matchResult = regex.matchEntire(input) ?: return null

    // Extract values and remove leading zeros
    val (month, day, year) = matchResult.destructured
    return "${month.toInt()}/${day.toInt()}/${year}"
}
