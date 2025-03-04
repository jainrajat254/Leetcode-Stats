package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.CustomTopBar
import com.example.leetcode.utils.TabContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        vm.updateAll()
    }
    Scaffold(
        containerColor = Color.Black,
        content = {
            UserStats(
                modifier = modifier,
                vm = vm,
                navController = navController
            )
        },
        bottomBar = { BottomNavBar(
            modifier = modifier,
            navController = navController
        ) }
    )
}

@Composable
fun UserStats(
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

        CustomTopBar(modifier = modifier, text = "Stats")

        Spacer(modifier = modifier.height(16.dp))

        TabContent(
            modifier = modifier,
            tabs = listOf("JAVA", "CPP"),
            contentForTab = { selectedTab ->
                {
                    when (selectedTab) {
                        "JAVA" -> {
                            UserStatsColumn(
                                language = "Java",
                                modifier = modifier,
                                vm = vm,
                                navController = navController
                            )
                        }

                        "CPP" -> {
                            UserStatsColumn(
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
fun UserStatsColumn(
    modifier: Modifier = Modifier,
    language: String,
    vm: ViewModel,
    navController: NavController,
) {
    var streakMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    // Fetch stats for the selected language
    LaunchedEffect(language) {
        streakMap = try {
            vm.questionsCount(language)
        } catch (e: Exception) {
            Log.e("UserStats", "Error fetching stats: ${e.localizedMessage}")
            emptyMap()
        }
    }

    LazyColumn(
        modifier = modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(streakMap.entries.toList()) { (name, score) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Routes.OtherProfile.createRoute(name)) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = name,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = score.toString(),
                            color = Color(0xFF00E676),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                }
            }
        }
    }
}

