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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.data.Stats
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.CommonTopBar
import com.example.leetcode.utils.EmptyState
import com.example.leetcode.utils.LoadingScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    vm: UserViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) { vm.updateAll() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            UserStats(modifier = modifier, vm = vm, navController = navController)
        },
        bottomBar = { BottomNavBar(navController = navController) }
    )
}

@Composable
fun UserStats(
    modifier: Modifier = Modifier,
    vm: UserViewModel,
    navController: NavController,
) {
    val pagerState = rememberPagerState { 2 } // Two pages (Java & C++)
    val languages = listOf("Java", "C++")
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 72.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CommonTopBar(title = "Stats") // Using the reusable top bar

        // Pager Tabs
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            languages.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(
                            text = title.uppercase(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (pagerState.currentPage == index)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                )
            }
        }

        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            UserStatsContent(
                language = languages[page],
                vm = vm,
                navController = navController
            )
        }
    }
}


@Composable
private fun UserStatsContent(
    language: String,
    vm: UserViewModel,
    navController: NavController,
) {
    val streakMap by produceState(initialValue = emptyList<Stats>(), language) {
        value = try {
            vm.questionsCount(language)
        } catch (e: Exception) {
            emptyList()
        }
    }

    val isLoading by remember { vm.isLoading }.collectAsState()

    when {
        isLoading -> LoadingScreen()
        streakMap.isEmpty() -> EmptyState(message = "No stats available")
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(streakMap) { stats ->
                    StatsListItem(
                        name = stats.name,
                        username = stats.username,
                        streakMap = stats.submissionCalendar.take(5).reversed(),
                        score = stats.totalSolved,
                        onClick = { navController.navigate(Routes.OtherProfile.createRoute(stats.username)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsListItem(
    name: String,
    username: String,
    streakMap: List<Boolean>,
    score: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "@$username",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Light
                    )
                )
            }

            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                streakMap.take(5).forEach { active ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                if (active) Color(0xFF65E26A) else Color(0xFFE45D5D),
                                shape = CircleShape
                            )
                    )
                }
            }

            Text(
                text = score.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}
