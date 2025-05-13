package com.sujoy.pbcadmin.presentation.Navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sujoy.pbcadmin.R
import com.sujoy.pbcadmin.presentation.Screens.InsertRoutineDataUI
import com.sujoy.pbcadmin.presentation.Screens.InsertRoutineUI
import com.sujoy.pbcadmin.presentation.Screens.WeburlUI
import com.sujoy.pbcadmin.presentation.ViewModel.AppViewModel
import org.tensorflow.lite.support.label.Category

@Composable
fun App(modifier: Modifier = Modifier, viewModel: AppViewModel) {
    var selectedIndex by remember { mutableStateOf(0) }
    val bottomBarItems = listOf(
        BottomBarItem("WebUrl", R.drawable.url),
        BottomBarItem("Routine", R.drawable.lesson),
        BottomBarItem("Class", R.drawable.lesson_class),
    )
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(110.dp),
                    containerColor = Color.Cyan
                ) {
                    bottomBarItems.forEachIndexed { index, bottomBarItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                Image(
                                    painter = painterResource(id = bottomBarItem.icon),
                                    modifier = Modifier.size(30.dp),
                                    contentDescription = null
                                )
                            },
                            label = { Text(text = bottomBarItem.title) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White,
                                selectedIconColor = Color.Black,
                                selectedTextColor = Color.Black,
                                unselectedIconColor = Color.White,
                                unselectedTextColor = Color.White
                            ))
                    }
                }
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                when (selectedIndex) {
                    0 -> WeburlUI(viewModel = viewModel)
                    1 -> InsertRoutineUI(viewModel = viewModel)
                    2 -> InsertRoutineDataUI(viewModel = viewModel)
                }
            }
        }
    }
}

data class BottomBarItem(val title: String, val icon: Int)