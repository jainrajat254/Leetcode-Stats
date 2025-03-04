package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.leetcode.R
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.CustomTopBar
import com.example.leetcode.utils.TabContent


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController = rememberNavController(),
) {
    LaunchedEffect(Unit) {
        vm.updateAll()
    }
    Scaffold(
        containerColor = Color.Black,
        content = {
            LeaderboardContent(
                modifier = modifier,
                vm = vm,
                navController = navController
            )
        },
        bottomBar = {
            BottomNavBar(
                modifier = modifier,
                navController = navController
            )
        }
    )
}

@Composable
fun LeaderboardContent(
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, bottom = 64.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTopBar(
            modifier = modifier,
            text = "Leaderboard"
        )
        Spacer(modifier = modifier.height(16.dp))

        // Tabs: Language & Club
        TabContent(
            modifier = modifier,
            tabs = listOf("Language", "Club"),
            contentForTab = { selectedTab ->
                {
                    when (selectedTab) {
                        "Language" -> LeaderboardEntries(
                            modifier = modifier,
                            vm = vm,
                            navController = navController
                        )
                        "Club" -> LeaderboardEntriesForClub(
                            modifier = modifier,
                            vm = vm,
                            navController = navController
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun LeaderboardEntries(
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Spacer(modifier = modifier.height(16.dp))
    // Tabs: JAVA & CPP
    TabContent(
        modifier = modifier,
        tabs = listOf("JAVA", "CPP"),
        contentForTab = { selectedTab ->
            {
                when (selectedTab) {
                    "JAVA" -> LeaderBoardByLanguage(
                        language = "Java",
                        modifier = modifier,
                        vm = vm,
                        navController = navController
                    )
                    "CPP" -> LeaderBoardByLanguage(
                        language = "CPP",
                        modifier = modifier,
                        vm = vm,
                        navController = navController
                    )
                }
            }
        }
    )
}

@Composable
fun LeaderBoardByLanguage(
    language: String,
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    var userList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(language) {
        try {
            userList = vm.languageLeaderBoard(language)
        } catch (e: Exception) {
            Log.e("LeaderBoardByLanguage", "Error: ${e.localizedMessage}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF101324))
            .padding(12.dp)
    ) {
        // Show top 3 if we have at least 3
        if (userList.size >= 3) {
            TopThreeSection(
                userList = userList,
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remaining users
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            items(userList.drop(3)) { name ->
                val rank = userList.indexOf(name) + 1

                LeaderboardListItem(
                    name = name,
                    rank = rank
                ) {
                    navController.navigate(Routes.OtherProfile.createRoute(name))
                }
            }
        }
    }
}

@Composable
fun LeaderboardEntriesForClub(
    modifier: Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    Spacer(modifier = modifier.padding(top = 16.dp))

    var userList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            userList = vm.clubLeaderBoard()
        } catch (e: Exception) {
            Log.e("LeaderboardEntriesForClub", "Error: ${e.localizedMessage}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF101324))
            .padding(12.dp)
    ) {
        // Show top 3 if we have at least 3
        if (userList.size >= 3) {
            TopThreeSection(
                userList = userList,
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remaining users
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            items(userList.drop(3)) { name ->
                val rank = userList.indexOf(name) + 1

                LeaderboardListItem(
                    name = name,
                    rank = rank
                ) {
                    navController.navigate(Routes.OtherProfile.createRoute(name))
                }
            }
        }
    }
}

@Composable
fun TopThreeSection(
    userList: List<String>,
    navController: NavController
) {
    val topThree = userList.take(3)
    val ranks = listOf(2, 1, 3)
    val imageSizes = listOf(40.dp, 40.dp, 40.dp)
    val heights = listOf(150.dp, 180.dp, 120.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        topThree.forEachIndexed { index, name ->
            val rank = ranks[index]
            val imageSize = imageSizes[index]
            val height = heights[index]
            val showCrown = (rank == 1)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate(Routes.OtherProfile.createRoute(name)) }
                    .height(height)
            ) {
                Box(contentAlignment = Alignment.TopCenter) {
                    Image(
                        painter = painterResource(id = R.drawable.leetcode),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .size(imageSize)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                    if (showCrown) {
                        Text(
                            text = "👑",
                            fontSize = 24.sp,
                            modifier = Modifier.offset(y = (-12).dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "@username",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = "Score: ${2000 - (rank - 1) * 300}",
                    color = Color(0xFFFFD700),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LeaderboardListItem(
    name: String,
    rank: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            Image(
                painter = painterResource(id = R.drawable.leetcode),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(50.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Name + handle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                )
                Text(
                    text = "@username",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Rank
            Text(
                text = rank.toString(),
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            )
        }
    }
}
