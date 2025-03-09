package com.example.leetcode.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.leetcode.data.StreakContent
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.CommonTopBar
import com.example.leetcode.utils.EmptyState
import com.example.leetcode.utils.LoadingScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(vm: UserViewModel, navController: NavController) {
    LaunchedEffect(Unit) { vm.updateAll() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavBar(navController = navController) },
        content = {
            StreakScreen(vm = vm, navController = navController)
        }
    )
}

@Composable
fun StreakScreen(vm: UserViewModel, navController: NavController) {
    val languages = listOf("Java", "C++")
    val pagerState = rememberPagerState { languages.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        CommonTopBar(title = "Streak") // Using the reusable top bar

        TabRow(selectedTabIndex = pagerState.currentPage) {
            languages.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title.uppercase()) }
                )
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            StudentStreak(language = languages[page], vm = vm, navController = navController)
        }
    }
}


@Composable
fun StudentStreak(language: String, vm: UserViewModel, navController: NavController) {
    var streakList by remember { mutableStateOf<List<StreakContent>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(language) {
        streakList = try {
            vm.hasAttemptedToday(language)
        } catch (e: Exception) {
            emptyList()
        }
        isLoading = false
    }

    when {
        isLoading -> LoadingScreen()
        streakList.isEmpty() -> EmptyState("No streak data available")
        else -> StreakList(streakList, navController)
    }
}

@Composable
fun StreakList(streaks: List<StreakContent>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, bottom = 0.dp, top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(streaks) { streak ->
            StreakListItem(
                name = streak.name,
                username = streak.username,
                isActive = streak.submittedToday,
                userAvatar = streak.userAvatar,
                onClick = { navController.navigate(Routes.OtherProfile.createRoute(streak.username)) }
            )
        }
    }
}

@Composable
fun StreakListItem(
    name: String,
    username: String,
    isActive: Boolean,
    userAvatar: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = userAvatar,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@$username",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(if (isActive) Color(0xFF65E26A) else Color(0xFFE45D5D))
        )
    }
}


