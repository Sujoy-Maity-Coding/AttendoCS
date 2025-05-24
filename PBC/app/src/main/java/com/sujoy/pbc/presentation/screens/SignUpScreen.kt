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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.sujoy.pbc.R
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.presentation.Util.DropdownMenuComponent
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.Util.getFileSizeInMB
import com.sujoy.pbc.presentation.Util.isPassportPhoto
import com.sujoy.pbc.presentation.navigation.Routes
import com.sujoy.pbc.presentation.viewmodel.AppViewModel
import com.sujoy.pbc.ui.theme.PrimaryColor

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    val viewModel: AppViewModel = hiltViewModel()
    val signUpScreenState by viewModel.signUpScreenState.collectAsState()

    var idNo by remember { mutableStateOf("") }
    var roll by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var registrationYear by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val fileSize = getFileSizeInMB(context, it)
                val isPassport = isPassportPhoto(context, it)

                if (fileSize > 1) {
                    Toast.makeText(context, "Image size must be less than 1 MB", Toast.LENGTH_SHORT)
                        .show()
                } else if (!isPassport) {
                    Toast.makeText(
                        context,
                        "Please select a passport-size photo (e.g., 600x800 or 3:4)",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    imageUri = it
                }
            }
        }

    LaunchedEffect(signUpScreenState) {
        signUpScreenState.userData?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(context, "Sign Up Successful😊", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.homeScreen) {
                popUpTo(Routes.LoginScreen) {
                    inclusive = true
                }
            }
        }
        signUpScreenState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        signUpScreenState.isLoading.takeIf { it }?.let {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg1up),
            colorFilter = ColorFilter.tint(Color(0x9444BBA4)),
            contentDescription = "upperbackground",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )
        Image(
            painter = painterResource(id = R.drawable.bg2), contentDescription = "lowerbackground",
            colorFilter = ColorFilter.tint(Color(0x9444BBA4)),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter), contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Your Account",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            // Image Picker
            if (imageUri == null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { launcher.launch("image/*") },
                    elevation = CardDefaults.cardElevation(5.dp),
                    border = BorderStroke(1.dp, PrimaryColor)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.face_id),
                            contentDescription = "SignUp",
                            modifier = Modifier.size(50.dp),
                            colorFilter = ColorFilter.tint(Color.Gray),
                            contentScale = ContentScale.FillBounds
                        )
                        Text(
                            text = "Select your passport size image with 3:4 ratio and size <= 1mb",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "profile img",
                    modifier = Modifier
                        .size(200.dp)
                        .border(1.dp, PrimaryColor, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = idNo,
                onValueChange = { idNo = it },
                label = { Text("Enter ID no.") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = PrimaryColor,
                    unfocusedIndicatorColor = PrimaryColor,
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = PrimaryColor
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = roll,
                onValueChange = { roll = it },
                label = { Text("Enter Roll") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = PrimaryColor,
                    unfocusedIndicatorColor = PrimaryColor,
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = PrimaryColor
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter Name") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = PrimaryColor,
                    unfocusedIndicatorColor = PrimaryColor,
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = PrimaryColor
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
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
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Enter Phone No.") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = PrimaryColor,
                    unfocusedIndicatorColor = PrimaryColor,
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = PrimaryColor
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = PrimaryColor,
                    unfocusedIndicatorColor = PrimaryColor,
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = PrimaryColor
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
                    focusedIndicatorColor = PrimaryColor,
                    unfocusedIndicatorColor = PrimaryColor,
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = PrimaryColor
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (idNo.isEmpty() || roll.isEmpty() || name.isEmpty() || department.isEmpty() || registrationYear.isEmpty() || semester.isEmpty() || phone.isEmpty() || email.isEmpty() || imageUri == null) {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    } else {
                        // Save reg year and dept to SharedPreferences
                        prefs.saveRegYear(registrationYear)
                        prefs.saveDepartment(department)
                        prefs.saveSemester(semester)

                        viewModel.createUser(
                            UserData(
                                idNo = idNo,
                                roll = roll,
                                Name = name,
                                department = department,
                                year = registrationYear,
                                semester = semester,
                                phone = phone,
                                email = email,
                                password = password
                            ),
                            registrationYear, department, imageUri, idNo
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(PrimaryColor),
                border = BorderStroke(2.dp, PrimaryColor)
            ) {
                Text(
                    "SignUp",
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
                    text = "Already have an account? ",
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Text(
                    text = "Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.LoginScreen)
                    })
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}