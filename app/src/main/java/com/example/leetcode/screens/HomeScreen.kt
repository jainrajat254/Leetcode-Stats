package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import com.example.leetcode.utils.CustomTopBar
import com.example.leetcode.utils.Indicator
import com.example.leetcode.utils.TabContent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Scaffold(containerColor = Color.Black, content = {
        StreakScreen(modifier = modifier, vm = vm)
    }, bottomBar = { BottomNavBar(modifier = modifier, navController = navController) })
}

@Composable
fun StreakScreen(modifier: Modifier, vm: ViewModel) {
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
                            StudentStreak(language = "Java", modifier = modifier, vm = vm)
                        }
                        "CPP" -> {
                            StudentStreak(language = "CPP", modifier = modifier, vm = vm)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun StudentStreak(language: String, modifier: Modifier, vm: ViewModel) {
    Spacer(modifier = modifier.padding(top = 16.dp))

    var streakMap by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    LaunchedEffect(language) {
        try {
            //vm.updateAll()
            streakMap = vm.hasAttemptedToday(language) // Fetching the data from ViewModel
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
        // Iterate over the keys of the map (usernames)
        items(streakMap.keys.toList()) { name ->
            val indicatorColor = streakMap[name] ?: false // Use the default value false if not found

            // Card for each user
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B)) // Card background color
            ) {
                val color = if (indicatorColor) Color(0xFF00FF00) else Color(0xFFFF4444) // Green if true, Red if false
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = name, color = Color.White) // User name
                    Indicator(modifier = modifier, color = color) // Streak indicator (Green/Red)
                }
            }
        }
    }
}

