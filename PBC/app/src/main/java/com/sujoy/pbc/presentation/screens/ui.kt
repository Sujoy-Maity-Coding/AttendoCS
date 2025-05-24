package com.sujoy.pbc.presentation.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sujoy.pbc.R
import com.sujoy.pbc.presentation.Util.AnimaterLottie
import com.sujoy.pbc.presentation.Util.DropdownMenuComponent
import com.sujoy.pbc.ui.theme.PrimaryColor
import com.sujoy.pbc.ui.theme.SecondaryColor

@Preview(showSystemUi = true)
@Composable
fun ui(modifier: Modifier = Modifier) {
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
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Image(painter = painterResource(id = R.drawable.bg1up), contentDescription = "upperbackground",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter), contentScale = ContentScale.FillWidth)
        Image(painter = painterResource(id = R.drawable.bg2), contentDescription = "lowerbackground",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter), contentScale = ContentScale.FillWidth)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Your Account",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier.size(200.dp),
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
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}