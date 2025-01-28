package com.example.leetcode.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.leetcode.R
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.utils.CustomTopBar
import com.example.leetcode.utils.TabContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController = rememberNavController(),
) {

    Scaffold(containerColor = Color.Black,
        content = { Leaderboard(modifier = modifier,vm = vm) },
        bottomBar = { BottomNavBar(modifier = modifier, navController = navController) }
    )
}

@Composable
fun Leaderboard(modifier: Modifier, vm: ViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, bottom = 64.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTopBar(modifier = modifier, text = "Leaderboard")
        Spacer(modifier = modifier.height(16.dp))

        TabContent(modifier = modifier,
            tabs = listOf("Language", "Club"),
            contentForTab = { selectedTab ->
                {
                    when (selectedTab) {
                        "Language" -> LeaderboardEntries(modifier = modifier, vm = vm)
                        "Club" -> LeaderboardEntriesForClub(modifier = modifier, vm = vm)
                    }
                }
            }
        )
    }
}

@Composable
fun LeaderboardEntries(modifier: Modifier, vm: ViewModel) {
    Spacer(modifier = modifier.height(16.dp))
    TabContent(
        modifier = modifier,
        tabs = listOf("JAVA", "CPP"),
        contentForTab = { selectedTab ->
            {
                when (selectedTab) {
                    "JAVA" -> LeaderBoardByLanguage(language = "Java", modifier = modifier, vm = vm)
                    "CPP" -> LeaderBoardByLanguage(language = "CPP", modifier = modifier, vm = vm)
                }
            }
        }
    )
}

@Composable
fun LeaderBoardByLanguage(language: String, modifier: Modifier, vm: ViewModel) {
    var list by remember { mutableStateOf<List<String>>(listOf()) }

    LaunchedEffect(language) {
        list = vm.languageLeaderBoard(selectedLanguage = language)
    }

    // LazyColumn to display the leaderboard
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        items(list) { name ->
            val rank = list.indexOf(name) + 1 // This will give the correct rank (starting from 1)

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B))
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.leetcode),
                        contentDescription = "User_Image",
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Transparent,
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = name,
                        color = Color.White,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        ),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = rank.toString(),
                        color = Color.White,
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        ),
                    )
                }
            }
        }
    }
}



@Composable
fun LeaderboardEntriesForClub(modifier: Modifier, vm: ViewModel) {
    Spacer(modifier = modifier.padding(top = 16.dp))

    var list by remember { mutableStateOf<List<String>>(listOf()) }
    //var streakList by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }

    LaunchedEffect(Unit) {
        list = vm.clubLeaderBoard()

//        val tempStreakList = mutableListOf<Pair<String, Int>>()
//
//        val results = list.map { user ->
//            async {
//                val stats = vm.getUserStats(user)
//                val totalSolved = stats?.totalSolved ?: 0
//                user to totalSolved
//            }
//        }
//
//        val streakResults = results.awaitAll()
//
//        streakResults.forEach { (user, totalSolved) ->
//            tempStreakList.add(user to totalSolved)
//        }
//
//        streakList = tempStreakList.sortedByDescending { it.second }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        items(list) { name ->
            val rank = list.indexOf(name) + 1

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B))
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.leetcode),
                        contentDescription = "User_Image",
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Transparent,
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = name,
                        color = Color.White,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        ),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = rank.toString(),
                        color = Color.White,
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        ),
                    )
                }
            }
        }
    }
}
