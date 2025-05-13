package com.sujoy.pbc.presentation.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.sujoy.pbc.common.Constant
import com.sujoy.pbc.data.api.RetrofitProvider
import com.sujoy.pbc.presentation.Util.AnimaterLottie
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.navigation.Routes
import com.sujoy.pbc.presentation.viewmodel.AppViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeUI(modifier: Modifier = Modifier,
           viewModel: AppViewModel = hiltViewModel(),
           firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
           navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val prefs = Prefs(context)
    val regYear = prefs.getRegYear()
    val dept = prefs.getDepartment()
    val profileState by viewModel.profileScreenState.collectAsStateWithLifecycle()
    val fetchWebUrlState by viewModel.getWebUrlState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()


    // Fetch the user data on screen load
    LaunchedEffect(key1 = true) {
        viewModel.getUserByUId(firebaseAuth.currentUser?.uid.toString(), regYear!!, dept!!)
    }

    if (profileState.isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center){
            AnimaterLottie(lottieRes = R.raw.lottie_hand_loading)
        }
    } else if (profileState.error != null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center) {
            Text(text = profileState.error!!, color = Color.Red)
        }
    } else if (fetchWebUrlState.error != null){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center) {
            Text(text = "Attedance sheet not found", color = Color.Red)
        }
    } else{
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .padding(15.dp)
            ) {
                Image(
                    painter = rememberImagePainter(profileState.userData?.userData?.image), contentDescription = "",
                    modifier = Modifier
                        .size(175.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
                Card(
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = 10.dp)
                        .clickable { navController.navigate(Routes.updateScreen) },
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_edit),
                        tint = Color.DarkGray,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    )
                }
            }
            Text(
                text = "${profileState.userData?.userData?.Name}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = FontFamily.Serif
            )
            Image(
                painter = painterResource(id = R.drawable.bg_green),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 17.dp)
                    .clickable { navController.navigate(Routes.profileScreen) },
                shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Card(
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                    Text(
                        text = "My Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 17.dp)
                    .clickable {
                        coroutineScope.launch {
                            viewModel.getWebUrl(
                                regYear!!,
                                dept!!,
                                profileState.userData?.userData?.semester.toString()
                            )
                            viewModel.getWebUrlState.collectLatest { fetchWebUrlState ->
                                when {
                                    fetchWebUrlState.isLoading -> {
                                        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                                    }

                                    !fetchWebUrlState.webUrl.isNullOrEmpty() -> {
                                        Constant.webUrl = fetchWebUrlState.webUrl
                                        RetrofitProvider.init(fetchWebUrlState.webUrl) // Initialize dynamically
                                        Toast.makeText(context, "WebUrl fetched successfully 😊", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Routes.attendanceScreen(id = profileState.userData?.userData?.idNo!!))
                                        return@collectLatest
                                    }

                                    fetchWebUrlState.error != null -> {
                                        Toast.makeText(context, fetchWebUrlState.error, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    },
                shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Card(
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.attendance),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                    Text(
                        text = "My Attendance",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 17.dp)
                    .clickable { navController.navigate(Routes.routineScreen(sem = profileState.userData?.userData?.semester.toString())) },
                shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Card(
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.routine),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                    Text(
                        text = "My Class Routine",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.LightGray)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.clickable {
                    firebaseAuth.signOut()
                    prefs.clearUserPrefs() // <- Clear SharedPreferences+
                    Toast.makeText(navController.context, "Logged Out🙃", Toast.LENGTH_SHORT)
                        .show()
                    // Make sure to navigate only if the NavController is ready
                    navController.currentBackStackEntry?.let {
                        navController.navigate(Routes.LoginScreen) {
                            popUpTo(0) // Clears the backstack
                        }
                    }
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(
                            Color.Gray
                        )
                    )
                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }
}