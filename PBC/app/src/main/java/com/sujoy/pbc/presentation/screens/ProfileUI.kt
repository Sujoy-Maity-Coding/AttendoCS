package com.sujoy.pbc.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.sujoy.pbc.R
import com.sujoy.pbc.presentation.Util.AnimaterLottie
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.navigation.Routes
import com.sujoy.pbc.presentation.viewmodel.AppViewModel

@Composable
fun ProfileUI(modifier: Modifier = Modifier,
              viewModel: AppViewModel = hiltViewModel(),
              firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
              navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val prefs = Prefs(context)
    val regYear = prefs.getRegYear()
    val dept = prefs.getDepartment()

    // Fetch the user data on screen load
    LaunchedEffect(key1 = true) {
        viewModel.getUserByUId(firebaseAuth.currentUser?.uid.toString(), regYear!!, dept!!)
    }

    // Collect the profile state from ViewModel
    val profileState by viewModel.profileScreenState.collectAsStateWithLifecycle()

    if (profileState.isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center){
            AnimaterLottie(lottieRes = R.raw.lottie_1)
        }
    } else if (profileState.error != null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center) {
            Text(text = profileState.error!!, color = Color.Red)
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .size(175.dp)
                .padding(15.dp)) {
                Image(
                    painter = rememberImagePainter(profileState.userData?.userData?.image), contentDescription = "",
                    modifier = Modifier
                        .size(175.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
                Card(modifier= Modifier
                    .size(35.dp)
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
                    .clickable { navController.navigate(Routes.updateScreen) },
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(5.dp)){
                    Icon(painter = painterResource(id = R.drawable.user_edit), tint = Color.DarkGray, contentDescription = "", modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp))
                }
            }
            Text(text = "${profileState.userData?.userData?.Name}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, fontFamily = FontFamily.Serif)
            Spacer(modifier = Modifier.height(15.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "ID",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.idNo}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "Roll",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.roll}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "Department",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.department}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "Semester",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.semester}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "Reg. Yr.",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.year}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "Phone",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.phone}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(modifier= Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 17.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
                    Card(modifier= Modifier
                        .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFF01BD2A)),
                        elevation = CardDefaults.cardElevation(5.dp)){
                        Text(text = "Email",fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "${profileState.userData?.userData?.email}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 15.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}