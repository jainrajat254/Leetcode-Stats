package com.example.leetcode.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.leetcode.R
import com.example.leetcode.data.Socials
import com.example.leetcode.models.ViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import com.example.leetcode.sharedPreferences.SharedPreferencesManager.getUserFromPreferences
import com.example.leetcode.utils.LastSevenDaysStreak
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) { vm.updateAll() }

    val context = LocalContext.current
    val userResponse = getUserFromPreferences(context)
    val displayName = userResponse?.name ?: "User Name"
    val username = userResponse?.username ?: "username"
    val selectedLanguage by rememberUpdatedState(userResponse?.selectedLanguage ?: "Java")

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            EnhancedDrawerContent(
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope,
                displayName = displayName,
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_menu_24),
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { ProfileHeaderSection(displayName,vm,username) }
                    item { QuestionStatsSection(username, vm) }
                    item { LastSevenDaysStreak(username = username, vm = vm, modifier = Modifier) }
                    item { UserStatsSection(selectedLanguage, username, vm) }
                    item { SocialLinksSection(username, vm) }
                }
            },
            bottomBar = {
                BottomNavBar(
                    modifier = Modifier.fillMaxWidth(),
                    navController = navController
                )
            }
        )
    }
}

@Composable
private fun ProfileHeaderSection(displayName: String,vm: ViewModel,username: String) {

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
            text = displayName,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
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
    selectedLanguage: String,
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
                value = selectedLanguage,
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
                value = String.format("%.2f%%", (questionsSolved.getOrNull(5)?.toFloatOrNull() ?: 0f)),
                iconRes = R.drawable.baseline_percent_24
            )
        }
    }
}

@Composable
private fun SocialLinksSection(username: String, vm: ViewModel) {

    var socials by remember { mutableStateOf(Socials("", "", "", "")) }

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
            url = socials.githubUrl?:"https://github.com",
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
fun SocialLinkItem(
    platform: String,
    url: String,
    iconRes: Int,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )
                    )
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = platform,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
private fun EnhancedDrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    displayName: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // Drawer Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        // Navigation Items
        DrawerItem(
            label = "Edit Profile",
            iconRes = R.drawable.baseline_edit_24,
            onClick = { /* Handle navigation */ }
        )
        DrawerItem(
            label = "Settings",
            iconRes = R.drawable.baseline_settings_24,
            onClick = { /* Handle navigation */ }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Footer
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)
        DrawerItem(
            label = "Log Out",
            iconRes = R.drawable.baseline_logout_24,
            labelColor = MaterialTheme.colorScheme.error,
            onClick = {
                SharedPreferencesManager.clearUserData(navController.context)
                navController.navigate(Routes.Login.route)
            }
        )
    }
}

@Composable
private fun DrawerItem(
    label: String,
    iconRes: Int,
    onClick: () -> Unit,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = labelColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(color = labelColor)
            )
        }
    }
}

@Composable
fun StatsRow(
    label: String,
    value: String,
    iconRes: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
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