package com.example.leetcode.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.utils.LastSevenDaysStreak

@Composable
fun OtherProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: ViewModel,
    username: String,
) {

    var nameAndLanguage by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        nameAndLanguage = vm.nameAndLanguage(username)
        vm.updateAll()
    }

    Scaffold(
        containerColor = Color.Black,
        content = { paddingValues ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Text(
                        text = username,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ProfileImage(
                            imageUri = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 12.dp)
                                .size(100.dp)
                                .border(width = 2.dp, color = Color.White, shape = CircleShape)
                        )
                        QuestionStats(
                            modifier = modifier,
                            username = username,
                            vm = vm
                        )
                    }
                }

                item {
                    Text(
                        text = nameAndLanguage.firstOrNull() ?: "Name",
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                item { SocialLinks(modifier = modifier, username_LC = username) }

                item { LastSevenDaysStreak(modifier = modifier, username, vm) }

                item {
                    UserStats(
                        modifier = modifier,
                        selectedLanguage = nameAndLanguage.getOrElse(1) { "Java" },
                        username = username,
                        vm = vm
                    )
                }
            }
        },
        bottomBar = { BottomNavBar(modifier = modifier, navController = navController) }
    )
}
