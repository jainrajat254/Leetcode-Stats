package com.example.leetcode.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.leetcode.R
import com.example.leetcode.data.Socials
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
    }

    LaunchedEffect(Unit) {
        Log.d("OtherProfileScreen", "Calling updateUser for $username")
        vm.updateUser(username)
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 20.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { ProfileHeaderSection(username, nameAndLanguage, vm) }
                item { QuestionStatsSection(username, vm) }
                item { LastSevenDaysStreak(username = username, vm = vm, modifier = Modifier) }
                item { UserStatsSection(nameAndLanguage, username, vm) }
                item { SocialLinksSection(username, vm) }
            }
        },
        bottomBar = { BottomNavBar(modifier = modifier, navController = navController) }
    )
}

@Composable
private fun ProfileHeaderSection(username: String, nameAndLanguage: List<String>, vm: ViewModel) {

    var profilePhoto by remember { mutableStateOf(Socials()) }

    LaunchedEffect(username) {
        profilePhoto = vm.getUserProfile(username)
    }

    Log.d("ProfileHeader", "Profile Photo URL: $profilePhoto") // Debugging log

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(
                    width = 3.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    shape = CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                model = profilePhoto.userAvatar,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = rememberAsyncImagePainter(R.drawable.baseline_person_24),
                placeholder = rememberAsyncImagePainter(R.drawable.baseline_person_24)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = nameAndLanguage.firstOrNull() ?: username,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = "@$username",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        )
    }
}


@Composable
private fun QuestionStatsSection(username: String, vm: ViewModel) {
    var questionsSolved by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        questionsSolved = try {
            vm.questionsSolved(username)
        } catch (e: Exception) {
            emptyList()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "Problem Solving Stats",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    number = questionsSolved.getOrNull(1) ?: "0",
                    tag = "Easy",
                    color = Color(0xFF00B8A3)
                )
                StatItem(
                    number = questionsSolved.getOrNull(2) ?: "0",
                    tag = "Medium",
                    color = Color(0xFFFFC01E)
                )
                StatItem(
                    number = questionsSolved.getOrNull(3) ?: "0",
                    tag = "Hard",
                    color = Color(0xFFFF375F)
                )
            }
        }
    }
}

@Composable
private fun UserStatsSection(
    nameAndLanguage: List<String>,
    username: String,
    vm: ViewModel,
) {
    var questionsSolved by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        questionsSolved = try {
            vm.questionsSolved(username)
        } catch (e: Exception) {
            emptyList()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Performance Overview",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            StatsRow(
                label = "Primary Language",
                value = nameAndLanguage.getOrElse(1) { "Java" },
                iconRes = R.drawable.baseline_code_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                label = "Total Solved",
                value = questionsSolved.getOrNull(0) ?: "0",
                iconRes = R.drawable.baseline_check_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                label = "LeetCode Rank",
                value = questionsSolved.getOrNull(4) ?: "0",
                iconRes = R.drawable.baseline_leaderboard_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                label = "Acceptance Rate",
                value = String.format(
                    "%.2f%%",
                    (questionsSolved.getOrNull(5)?.toFloatOrNull() ?: 0f)
                ),
                iconRes = R.drawable.baseline_percent_24
            )
        }
    }
}

@Composable
private fun SocialLinksSection(username: String, vm: ViewModel) {

    var socials by remember { mutableStateOf(Socials()) }

    LaunchedEffect(Unit) {
        socials = vm.getUserSocials(username)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Social Profiles",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
        )

        SocialLinkItem(
            platform = "LeetCode",
            url = "https://leetcode.com/u/$username",
            iconRes = R.drawable.leetcode
        )
        SocialLinkItem(
            platform = "GitHub",
            url = socials.githubUrl ?: "https://gihub.com",
            iconRes = R.drawable.github
        )
        SocialLinkItem(
            platform = "LinkedIn",
            url = socials.linkedinUrl ?: "https://linkedin.com",
            iconRes = R.drawable.linkedin
        )
    }
}

@Composable
private fun StatItem(
    number: String,
    tag: String,
    color: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 32.sp
            )
        )
        Text(
            text = tag,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        )
    }
}