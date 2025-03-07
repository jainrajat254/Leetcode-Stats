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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.leetcode.R
import com.example.leetcode.data.LeaderBoard
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController = rememberNavController(),
) {
    LaunchedEffect(true) { vm.updateAll() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            LeaderboardContent(
                modifier = modifier,
                vm = vm,
                navController = navController
            )
        },
        bottomBar = {
            BottomNavBar(
                modifier = Modifier.fillMaxWidth(),
                navController = navController
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardContent(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "LeaderBoard",
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

        // Tabs for Club and Language
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            listOf("Club", "Language").forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                ) {
                    Text(title, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // HorizontalPager for tab content
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> ClubLeaderboard(vm, navController)
                1 -> LanguageLeaderboard(vm, navController)
            }
        }
    }

}

@Composable
private fun LeaderboardList(
    leaderboard: List<LeaderBoard>,
    navController: NavController
) {
    if (leaderboard.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No entries found")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TopThreeSection(users = leaderboard.take(3), navController = navController)
                Spacer(Modifier.height(16.dp))
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
private fun LanguageLeaderboard(
    vm: ViewModel,
    navController: NavController,
) {
    var selectedLanguage by remember { mutableStateOf("Java") }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var leaderboardEntries by remember { mutableStateOf<List<LeaderBoard>>(emptyList()) }

    val languages = listOf("Java", "C++")

    // Fetch leaderboard data when the language changes
    LaunchedEffect(selectedLanguage) {
        loading = true
        error = null
        try {
            leaderboardEntries = vm.languageLeaderBoard(selectedLanguage)
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = languages.indexOf(selectedLanguage),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            languages.forEachIndexed { index, language ->
                Tab(
                    selected = selectedLanguage == language,
                    onClick = { selectedLanguage = language },
                    text = { Text(language, style = MaterialTheme.typography.bodyMedium) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            error != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error", color = Color.Red)
            }
            else -> LeaderboardList(leaderboardEntries, navController)
        }
    }
}


@Composable
fun LoadableContent(
    loading: Boolean,
    error: String?,
    content: @Composable () -> Unit
) {
    when {
        loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        error != null -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        else -> content()
    }
}


@Composable
private fun ClubLeaderboard(
    vm: ViewModel,
    navController: NavController,
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var leaderboardEntries by remember { mutableStateOf<List<LeaderBoard>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            leaderboardEntries = vm.clubLeaderBoard()
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    when {
        loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        error != null -> Text("Error: $error", color = Color.Red, modifier = Modifier.fillMaxSize())
        else -> LeaderboardList(leaderboardEntries, navController)
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
            val imageSize = if (index == 0) 100.dp else if (index == 1)  80.dp else 60.dp // Make the 1st place larger
            val columnHeight = if (index == 0) 160.dp else if (index == 1) 140.dp else 120.dp// Increase height for 1st

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .height(columnHeight)
                    .clickable { navController.navigate(Routes.OtherProfile.createRoute(user.username)) }
            ) {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .border(
                            3.dp,
                            medalColors[index],
                            CircleShape
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

                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
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
                .padding(16.dp),
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

            // Username (with weight to take remaining space)
            Text(
                text = entry.username,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            // Rank on the Right
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

