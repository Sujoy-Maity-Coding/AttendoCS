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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.sujoy.pbc.R
import com.sujoy.pbc.domain.model.UserData
import com.sujoy.pbc.domain.model.UserDataParent
import com.sujoy.pbc.presentation.Util.AnimaterLottie
import com.sujoy.pbc.presentation.Util.DropdownMenuComponent
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.Util.getFileSizeInMB
import com.sujoy.pbc.presentation.Util.isPassportPhoto
import com.sujoy.pbc.presentation.navigation.Routes
import com.sujoy.pbc.presentation.viewmodel.AppViewModel
import com.sujoy.pbc.ui.theme.PrimaryColor

@Composable
fun UpdateUI(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val prefs = Prefs(context)
    val regYear = prefs.getRegYear()
    val dept = prefs.getDepartment()

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

    // Fetch the user data on screen load
    LaunchedEffect(key1 = true) {
        viewModel.getUserByUId(firebaseAuth.currentUser?.uid.toString(), regYear!!, dept!!)
    }

    // Collect the profile state from ViewModel
    val profileState by viewModel.profileScreenState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateProfileScreenState.collectAsStateWithLifecycle()

    val hasUpdateBeenTriggered = remember { mutableStateOf(false) }

    LaunchedEffect(updateState) {
        if (hasUpdateBeenTriggered.value) {
            if (!updateState.isLoading && updateState.error == null) {
                Toast.makeText(context, "Profile Updated 🎉", Toast.LENGTH_SHORT).show()
                hasUpdateBeenTriggered.value = false
            } else if (updateState.error != null) {
                Toast.makeText(context, updateState.error, Toast.LENGTH_SHORT).show()
                hasUpdateBeenTriggered.value = false
            }
        }
    }


    if (profileState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AnimaterLottie(lottieRes = R.raw.lottie_1)
        }
    } else if (profileState.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = profileState.error!!, color = Color.Black)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Edit screen🎉",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(20.dp))
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
                            contentDescription = "Updated profile",
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

            var name by remember {
                mutableStateOf(
                    profileState.userData?.userData?.Name ?: ""
                )
            }
            var phone by remember {
                mutableStateOf(
                    profileState.userData?.userData?.phone ?: ""
                )
            }
            var idNo by remember {
                mutableStateOf(
                    profileState.userData?.userData?.idNo ?: ""
                )
            }
            var roll by remember {
                mutableStateOf(
                    profileState.userData?.userData?.roll ?: ""
                )
            }
            var semester by remember {
                mutableStateOf(
                    profileState.userData?.userData?.semester ?: ""
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
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
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .width(320.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = idNo,
                onValueChange = { idNo = it },
                label = { Text("ID no.") },
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
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .width(320.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = roll,
                onValueChange = { roll = it },
                label = { Text("Roll") },
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
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .width(320.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            DropdownMenuComponent(
                "Select Semester",
                listOf("1", "2", "3", "4", "5", "6", "7", "8")
            ) {
                semester = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone No.") },
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
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .width(320.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    prefs.updateSemester(semester)
                    hasUpdateBeenTriggered.value=true
                    val updatedUserData = UserData(
                        idNo = idNo,
                        roll = roll,
                        Name = name,
                        department = profileState.userData?.userData?.department ?: "",
                        year = profileState.userData?.userData?.year ?: "",
                        semester = semester,
                        phone = phone,
                        email = profileState.userData?.userData?.email ?: "",
                        password = profileState.userData?.userData?.password ?: "",
                        image = profileState.userData?.userData?.image ?: "",
                        Uid = profileState.userData?.userData?.Uid ?: ""
                    )
                    viewModel.updateUserData(
                        UserDataParent(
                            nodeId = profileState.userData?.nodeId ?: "",
                            userData = updatedUserData
                        ),
                        regYear!!,
                        dept!!,
                        imageUri
                    )
                },
                colors = ButtonDefaults.buttonColors(PrimaryColor),
                modifier = Modifier.width(317.dp)
            ) {
                if (updateState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}