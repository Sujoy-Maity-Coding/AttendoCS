package com.sujoy.pbcadmin.presentation.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sujoy.pbcadmin.presentation.Utils.DropdownMenuComponent

@Preview(showSystemUi = true)
@Composable
fun UI(modifier: Modifier = Modifier) {
    var year by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    var semester by remember { mutableStateOf("") }
    var semesterUrl by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                Color.Cyan,
                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            )
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
            contentAlignment = Alignment.Center){
            Text(text = "WebUrl Insert", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Serif)
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
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
}