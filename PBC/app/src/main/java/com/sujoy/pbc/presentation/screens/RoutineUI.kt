package com.sujoy.pbc.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.sujoy.pbc.R
import com.sujoy.pbc.presentation.Util.AnimaterLottie
import com.sujoy.pbc.presentation.Util.Prefs
import com.sujoy.pbc.presentation.navigation.Routes
import com.sujoy.pbc.presentation.viewmodel.AppViewModel

@Composable
fun RoutineUI(
    viewModel: AppViewModel = hiltViewModel(),
    args: Routes.routineScreen
) {
    val context = LocalContext.current
    val prefs = Prefs(context)
    val regYear = prefs.getRegYear()
    val dept = prefs.getDepartment()
    var imageHeight by remember { mutableStateOf(0) }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp

    // Fetch the user data on screen load
    LaunchedEffect(key1 = true) {
        viewModel.getRoutineImageUrl(regYear!!, dept!!, args.sem)
    }

    val getRoutineImageUrl by viewModel.getRoutineScreenState.collectAsStateWithLifecycle()

    if (getRoutineImageUrl.isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center){
            AnimaterLottie(lottieRes = R.raw.lottie_1)
        }
    } else if (getRoutineImageUrl.error != null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center){
            Text(text = "${getRoutineImageUrl.error}", fontSize = 20.sp, color = Color.Red)
        }
    } else if(getRoutineImageUrl.imageUrl != null){
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
                        Color.Green,
                        shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                    )
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Routine Screen",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Semester: ${args.sem}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (imageHeight > 0) Modifier.height(imageHeight.dp) else Modifier
                    )
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .graphicsLayer(
                        scaleX = scale.coerceIn(1f, 5f),
                        scaleY = scale.coerceIn(1f, 5f),
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale *= zoom
                            offset += pan
                        }
                    }
                    .transformable(state)
            ) {
                SubcomposeAsyncImage(
                    model = getRoutineImageUrl.imageUrl,
                    contentDescription = "routine img",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Text(
                            text = "Loading...",
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )
                    },
                    onSuccess = { success ->
                        val painter = success.result.drawable
                        val ratio = painter.intrinsicHeight.toFloat() / painter.intrinsicWidth
                        imageHeight = (screenWidth * ratio).toInt()
                    }
                )
            }
        }
    }else{
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center){
            Text(text = "Routine not found🥲", color = Color.Red)
        }
    }
}