package com.example.leetcode.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.utils.LastThirtyDays
import com.example.leetcode.utils.ProfileHeaderSection
import com.example.leetcode.utils.QuestionStatsSection
import com.example.leetcode.utils.SocialLinksSection
import com.example.leetcode.utils.UserStatsSection

@Composable
fun OtherProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: UserViewModel,
    username: String,
) {
    var nameAndLanguage by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        nameAndLanguage = vm.nameAndLanguage(username)
    }

    LaunchedEffect(Unit) {
        vm.updateUser(username)
    }


    Scaffold(containerColor = MaterialTheme.colorScheme.background, content = { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 20.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ProfileHeaderSection(
                    username = username,
                    displayName = nameAndLanguage.firstOrNull() ?: username,
                    vm = vm
                )
            }
            item { QuestionStatsSection(username, vm) }
            item { LastThirtyDays(username = username, vm = vm, modifier = Modifier) }
            item {
                UserStatsSection(
                    primaryLanguage = nameAndLanguage.getOrElse(1) { "Java" },
                    username = username,
                    vm = vm
                )
            }
            item { SocialLinksSection(username, vm) }
        }
    }, bottomBar = { BottomNavBar(navController = navController) })
}
