package com.sujoy.pbccs.Presentation.Screens

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.sujoy.pbccs.Presentation.Navigation.Routes
import com.sujoy.pbccs.Presentation.Util.AnimaterLottie
import com.sujoy.pbccs.Presentation.ViewModel.AttendanceViewModel
import com.sujoy.pbccs.Presentation.ViewModel.FetchStudentState
import com.sujoy.pbccs.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel,
    navController: NavHostController,
    args: Routes.Attendance
) {
    val students by viewModel.fetchStudentState.collectAsState()
    val markAttendanceState by viewModel.markAttendanceState.collectAsState()
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    LaunchedEffect(Unit) {
        viewModel.fetchStudents(args.paper)
    }

    // Show toast messages when attendance is marked
    LaunchedEffect(markAttendanceState) {
        markAttendanceState.isAttendanceMarked.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        markAttendanceState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        markAttendanceState.isLoading.takeIf { it }?.let {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
        }
    }

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
                .background(
                    Color(0xFF0E8AEC),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Student Attendence List",
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            when {
                students.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimaterLottie(lottieRes = R.raw.lottie_2, modifier = Modifier.size(200.dp))
                    }
                }

                students.error != null -> {
                    Text(text = "Error: ${students.error}", color = MaterialTheme.colorScheme.error)
                }

                students.students.isNotEmpty() -> {
                    val pagerState = rememberPagerState(pageCount = { students.students.size })

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        key = { students.students[it].id } // Ensures each student has a unique key
                    ) { page ->
                        val student = students.students[page]
                        StudentCard(
                            state=pagerState,
                            students=students,
                            studentId = student.id,
                            regYr = args.regYr,
                            department = args.department,
                            viewModel = viewModel,
                            context=context,
                            paper=args.paper,
                            key = student.id
                        ) { studentId, status ->
                            viewModel.markAttendance(
                                sheet = args.paper,
                                date = args.date,
                                studentId = studentId,
                                status = status
                            )
                        }
                    }
                }

                else -> {
                    Text(text = "No students found.", modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}
@Composable
fun StudentCard(
    studentId: String,
    regYr: String,
    department: String,
    viewModel: AttendanceViewModel,
    key: String,
    paper: String,
    context: Context,
    students: FetchStudentState,
    state: PagerState,
    onMarkAttendance: (String, String) -> Unit,
) {
    Log.d("StudentCard", "Displaying Student ID: $studentId")

    val getStudentByIdState by viewModel.getStudentByIdState.collectAsState()
    val fetchAttendanceState by viewModel.fetchAttendanceState.collectAsState()
    val currentState by rememberUpdatedState(getStudentByIdState)

    val currentStudentId = students.students.getOrNull(state.currentPage)?.id

    LaunchedEffect(currentStudentId) {
        if (!currentStudentId.isNullOrEmpty()) {
            viewModel.getStudentById(regYr, department, currentStudentId)
            viewModel.fetchAttendanceSummary(sheet = paper, studentId = currentStudentId)
        }
    }

    Card(
        modifier = Modifier.height(450.dp),
        colors = CardDefaults.cardColors(Color(0x9FFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                currentState.isLoading && fetchAttendanceState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimaterLottie(lottieRes = R.raw.lottie_img, modifier = Modifier.size(150.dp))
                    }
                }

                currentState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${currentState.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                fetchAttendanceState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${fetchAttendanceState.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                currentState.studentData != null && fetchAttendanceState.attendanceSummary != null -> {
                    val student = currentState.studentData!!
                    Log.d("StudentCard", "Student Data Found: $student")
                    Image(
                        painter = rememberImagePainter(student.image),
                        contentDescription = "profile img",
                        modifier = Modifier
                            .size(150.dp)
                            .border(1.dp, Color(0xFF0E8AEC), CircleShape)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Roll:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = student.roll,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E8AEC)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "ID:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = student.idNo,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E8AEC)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = student.Name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E8AEC)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row{
                            Text(
                                text = "T_Class:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${fetchAttendanceState.attendanceSummary?.totalClass}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E8AEC)
                            )
                        }
                        Row{
                            Text(
                                text = "A_Class:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${fetchAttendanceState.attendanceSummary?.totalAttendance}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E8AEC)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Percentage:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val percentage = String.format("%.2f", fetchAttendanceState.attendanceSummary?.attendancePercentage ?: 0.0)
                        Text(
                            text = "${percentage}%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E8AEC)
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onMarkAttendance(student.idNo, "P") },
                            modifier = Modifier
                                .width(100.dp)
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            androidx.compose.material.Text(
                                text = "P",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                        Button(
                            onClick = { onMarkAttendance(student.idNo, "A") },
                            modifier = Modifier
                                .width(100.dp)
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            androidx.compose.material.Text(
                                text = "A",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                !currentState.isLoading && currentState.studentData == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Student not found",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Red
                        )
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimaterLottie(lottieRes = R.raw.lottie_img, modifier = Modifier.size(150.dp))
                    }
                }
            }
        }
    }
}
