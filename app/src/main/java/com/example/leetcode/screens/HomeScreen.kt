package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.CustomTopBar
import com.example.leetcode.utils.Indicator
import com.example.leetcode.utils.TabContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        vm.updateAll()
    }
    Scaffold(
        containerColor = Color(0xFF121212),
        content = {
            StreakScreen(
                modifier = modifier,
                vm = vm,
                navController = navController
            )
        },
        bottomBar = { BottomNavBar(modifier = modifier, navController = navController) }
    )
}

@Composable
fun StreakScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTopBar(modifier = modifier, text = "Streak")
        Spacer(modifier = modifier.height(16.dp))
        TabContent(
            modifier = modifier,
            tabs = listOf("Java", "C++"),
            contentForTab = { selectedTab ->
                {
                    StudentStreak(
                        language = selectedTab,
                        modifier = modifier,
                        vm = vm,
                        navController = navController
                    )
                }
            }
        )
    }
}

@Composable
fun StudentStreak(
    language: String,
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    var streakMap by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    LaunchedEffect(language) {
        streakMap = try {
            vm.hasAttemptedToday(language)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        items(streakMap.keys.toList()) { username ->
            val isActive = streakMap[username] ?: false
            val indicatorColor = if (isActive) Color(0xFF00E676) else Color(0xFFFF5252)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Routes.OtherProfile.createRoute(username)) }
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = username,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Indicator(
                        modifier = Modifier,
                        color = indicatorColor
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Gray, thickness = 0.5.dp)
            }
        }
    }
}
