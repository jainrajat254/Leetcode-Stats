package com.example.leetcode.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) { vm.updateAll() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val languages = listOf("Java", "C++")

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Streak",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            languages.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title.uppercase(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (selectedTabIndex == index)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                )
            }
        }

        StudentStreak(
            language = languages[selectedTabIndex],
            modifier = modifier,
            vm = vm,
            navController = navController
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
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(language) {
        streakMap = try {
            vm.hasAttemptedToday(language).also { loading = false }
        } catch (e: Exception) {
            loading = false
            emptyMap()
        }
    }

    when {
        loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        streakMap.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No streak data available",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp, bottom = 60.dp, top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(streakMap.entries.toList()) { (username, isActive) ->
                    StreakListItem(
                        username = username,
                        isActive = isActive,
                        onClick = { navController.navigate(Routes.OtherProfile.createRoute(username)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StreakListItem(
    username: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isActive) Color(0x9900FF00)
                        else Color(0x99FF4444),
                        shape = CircleShape
                    )
            )
        }
    }
}