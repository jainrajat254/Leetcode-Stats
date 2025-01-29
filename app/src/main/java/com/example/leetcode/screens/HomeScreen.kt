package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Scaffold(
        containerColor = Color.Black,
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
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, bottom = 64.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTopBar(modifier = modifier, text = "Streak")
        Spacer(modifier = modifier.height(16.dp))
        TabContent(
            modifier = modifier,
            tabs = listOf("JAVA", "CPP"),
            contentForTab = { selectedTab ->
                {
                    when (selectedTab) {
                        "JAVA" -> {
                            StudentStreak(
                                language = "Java",
                                modifier = modifier,
                                vm = vm,
                                navController = navController
                            )
                        }

                        "CPP" -> {
                            StudentStreak(
                                language = "CPP",
                                modifier = modifier,
                                vm = vm,
                                navController = navController
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun StudentStreak(
    language: String,
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Spacer(modifier = modifier.padding(top = 16.dp))

    var streakMap by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    LaunchedEffect(language) {
        try {
            streakMap = vm.hasAttemptedToday(language)
        } catch (e: Exception) {
            Log.e("StudentStreak", "Error fetching streak data: ${e.localizedMessage}")
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        items(streakMap.keys.toList()) { username ->
            val indicatorColor = streakMap[username] ?: false

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navController.navigate(Routes.OtherProfile.createRoute(username)) },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B))
            ) {
                val color = if (indicatorColor) Color(0xFF00FF00) else Color(0xFFFF4444)
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = username,
                        color = Color.White
                    )
                    Indicator(
                        modifier = modifier,
                        color = color
                    )
                }
            }
        }
    }
}
