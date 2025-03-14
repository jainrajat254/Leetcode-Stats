package com.example.leetcode.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.leetcode.R
import com.example.leetcode.data.LeaderBoard
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.CommonTabRow
import com.example.leetcode.utils.CommonTopBar
import com.example.leetcode.utils.FilterBottomSheet
import com.example.leetcode.utils.FilterFAB
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderboardScreen(
    vm: UserViewModel,
    navController: NavController = rememberNavController(),
) {
    LaunchedEffect(true) { vm.updateAll() }

    var showFilterDialog by remember { mutableStateOf(false) } // Move state here

    Scaffold(containerColor = MaterialTheme.colorScheme.background,
        content = {
            LeaderboardContent(
                vm = vm, navController = navController,
                showFilterDialog
            ) {
                showFilterDialog = it
            }
        }, bottomBar = {
            BottomNavBar(navController = navController)
        },
        floatingActionButton = {
            FilterFAB(onClick = { showFilterDialog = true })
        },
        )
}

@Composable
fun LeaderboardContent(
    vm: UserViewModel,
    navController: NavController,
    showFilterDialog: Boolean,
    setShowFilterDialog: (Boolean) -> Unit,
) {
    val tabs = listOf("Club", "Language")
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CommonTopBar(title = "Leaderboard") // Using the reusable top bar

        Spacer(modifier = Modifier.height(8.dp))

        CommonTabRow(
            tabs = tabs,
            selectedIndex = pagerState.currentPage,
            onTabSelected = { index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } }
        )

        HorizontalPager(
            state = pagerState, modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ClubLeaderboard(vm = vm,
                    navController = navController,
                    showFilterDialog = showFilterDialog,
                    setShowFilterDialog = setShowFilterDialog)
                1 -> LanguageLeaderboard(vm = vm,
                    navController = navController,
                    showFilterDialog = showFilterDialog,
                    setShowFilterDialog = setShowFilterDialog)
            }
        }
    }
}


@Composable
private fun LeaderboardList(
    leaderboard: List<LeaderBoard>,
    navController: NavController,
) {
    if (leaderboard.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No entries found")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 84.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TopThreeSection(users = leaderboard.take(3), navController = navController)
            }
            itemsIndexed(leaderboard.drop(3)) { index, entry ->
                LeaderboardItem(rank = index + 4, entry = entry) {
                    navController.navigate(Routes.OtherProfile.createRoute(entry.username))
                }
            }
        }
    }
}


@Composable
private fun ClubLeaderboard(
    vm: UserViewModel,
    navController: NavController,
    showFilterDialog: Boolean,
    setShowFilterDialog: (Boolean) -> Unit,
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var leaderboardEntries by remember { mutableStateOf<List<LeaderBoard>>(emptyList()) }

    LaunchedEffect(Unit) {
        loading = true
        error = null
        try {
            leaderboardEntries = vm.clubLeaderBoard()
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    var selectedYear by remember { mutableStateOf("All") }
    var minQuestions by remember { mutableStateOf("") }

    val years = listOf("All") + leaderboardEntries.map { it.year }.distinct()

    val filteredList = leaderboardEntries.filter { stats ->
        val matchesYear = selectedYear == "All" || stats.year == selectedYear
        val matchesQuestionCount =
            minQuestions.toIntOrNull()?.let { stats.totalSolved >= it } ?: true
        matchesYear && matchesQuestionCount
    }

    if (showFilterDialog) {
        FilterBottomSheet(
            years = years,
            selectedYear = selectedYear,
            onYearSelected = { selectedYear = it },
            minQuestions = minQuestions,
            onMinQuestionsChange = { minQuestions = it },
            onDismiss = { setShowFilterDialog(false) }
        )
    }

    LeaderboardContent(
        loading = loading,
        error = error,
        leaderboardEntries = filteredList,
        navController = navController
    )
}

@Composable
private fun LanguageLeaderboard(
    vm: UserViewModel,
    navController: NavController,
    showFilterDialog: Boolean,
    setShowFilterDialog: (Boolean) -> Unit,
) {
    val languages = listOf("Java", "C++")
    val pagerState = rememberPagerState { languages.size }
    val coroutineScope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var leaderboardEntries by remember { mutableStateOf<List<LeaderBoard>>(emptyList()) }

    // Update data when the selected tab changes
    LaunchedEffect(pagerState.currentPage) {
        loading = true
        error = null
        try {
            val selectedLanguage = languages[pagerState.currentPage]
            leaderboardEntries = vm.languageLeaderBoard(selectedLanguage)
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CommonTabRow(
            tabs = languages,
            selectedIndex = pagerState.currentPage,
            onTabSelected = { index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        var selectedYear by remember { mutableStateOf("All") }
        var minQuestions by remember { mutableStateOf("") }

        val years = listOf("All") + leaderboardEntries.map { it.year }.distinct()

        val filteredList = leaderboardEntries.filter { stats ->
            val matchesYear = selectedYear == "All" || stats.year == selectedYear
            val matchesQuestionCount =
                minQuestions.toIntOrNull()?.let { stats.totalSolved >= it } ?: true
            matchesYear && matchesQuestionCount
        }

        if (showFilterDialog) {
            FilterBottomSheet(
                years = years,
                selectedYear = selectedYear,
                onYearSelected = { selectedYear = it },
                minQuestions = minQuestions,
                onMinQuestionsChange = { minQuestions = it },
                onDismiss = { setShowFilterDialog(false) }
            )
        }

        HorizontalPager(
            state = pagerState, modifier = Modifier.weight(1f)
        ) {
            LeaderboardContent(
                loading = loading,
                error = error,
                leaderboardEntries = filteredList,
                navController = navController
            )
        }
    }
}


@Composable
private fun LeaderboardContent(
    loading: Boolean,
    error: String?,
    leaderboardEntries: List<LeaderBoard>,
    navController: NavController,
) {
    when {
        loading -> Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        error != null -> Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text("Error: $error", color = Color.Red)
        }

        else -> LeaderboardList(leaderboardEntries, navController = navController)
    }
}

@Composable
private fun TopThreeSection(
    users: List<LeaderBoard>,
    navController: NavController,
) {
    val medalColors = listOf(
        Color(0xFFFFD700), // Gold
        Color(0xFFC0C0C0), // Silver
        Color(0xFFCD7F32)  // Bronze
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        users.forEachIndexed { index, user ->
            val imageSize =
                if (index == 0) 100.dp else if (index == 1) 80.dp else 60.dp // Largest for 1st place
            val columnHeight =
                if (index == 0) 160.dp else if (index == 1) 140.dp else 120.dp // Taller for 1st

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .height(columnHeight)
                    .clickable { navController.navigate(Routes.OtherProfile.createRoute(user.username)) }) {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .border(
                            3.dp, medalColors[index], CircleShape
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = user.userAvatar,
                        contentDescription = "${user.username}'s avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(8.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Name (Bold, prominent)
                    Text(
                        text = user.name, style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )

                    // Username (Dimmed, smaller)
                    Text(
                        text = "@${user.username}", style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Light
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardItem(
    rank: Int,
    entry: LeaderBoard,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile Image
            AsyncImage(
                model = entry.userAvatar,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.baseline_person_24),
                error = painterResource(id = R.drawable.baseline_person_24)
            )

            // Name & Username Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                // Name (Bold, prominent)
                Text(
                    text = entry.name, style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface
                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )

                // Username (Smaller & dimmed)
                Text(
                    text = "@${entry.username}", style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Light
                    )
                )
            }

            // Rank on the Right (Takes Less Space)
            Text(
                text = "#$rank", style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                ), modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
