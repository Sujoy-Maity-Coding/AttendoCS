package com.sujoy.pbc.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.sujoy.pbc.R
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.presentation.Util.DropdownMenuComponent
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.navigation.Routes
import com.sujoy.pbc.presentation.viewmodel.AppViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    val viewModel: AppViewModel = hiltViewModel()
    val loginScreenState by viewModel.signInScreenState.collectAsState()

    var department by remember { mutableStateOf("") }
    var registrationYear by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    LaunchedEffect(loginScreenState) {
        loginScreenState.userData?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(context, "Login Successful😊", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.homeScreen){
                popUpTo(Routes.LoginScreen){
                    inclusive = true
                }
            }
        }
        loginScreenState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        loginScreenState.isLoading.takeIf { it }?.let {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomEnd)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)
                )
                .clip(shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp))
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF00EC33)
            )
            Spacer(modifier = Modifier.height(15.dp))
            DropdownMenuComponent(
                "Select registration Year",
                listOf("2023", "2024", "2025")
            ) {
                registrationYear = it
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF00EC33),
                    unfocusedIndicatorColor = Color(0xFF00EC33),
                    cursorColor = Color(0xFF00EC33),
                    focusedLabelColor = Color(0xFF00EC33),
                    unfocusedLabelColor = Color(0xFF00EC33)
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF00EC33),
                    unfocusedIndicatorColor = Color(0xFF00EC33),
                    cursorColor = Color(0xFF00EC33),
                    focusedLabelColor = Color(0xFF00EC33),
                    unfocusedLabelColor = Color(0xFF00EC33)
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (registrationYear.isEmpty() || department.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }else {
                        // Save reg year and dept to SharedPreferences
                        prefs.saveRegYear(registrationYear)
                        prefs.saveDepartment(department)
                        prefs.saveSemester(semester)

                        viewModel.signInUser(
                            userData = UserData(
                                idNo = "",
                                roll = "",
                                Name = "",
                                department = "",
                                year = "",
                                semester = "",
                                phone = "",
                                email = email,
                                password = password,
                                image = ""
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00EC33)),
                border = BorderStroke(2.dp, Color(0xFF00EC33))
            ) {
                Text(
                    "Login",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Text(
                    text = "SignUp",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.signUpScreen)
                    })
            }
        }
    }
}