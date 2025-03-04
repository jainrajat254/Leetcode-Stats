package com.example.leetcode.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.leetcode.R
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
    // Update all data when screen launches.
    LaunchedEffect(Unit) {
        vm.updateAll()
    }

    val context = LocalContext.current
    val userResponse = getUserFromPreferences(context)
    val username = userResponse?.username ?: "UserName"
    val selectedLanguage by rememberUpdatedState(userResponse?.selectedLanguage ?: "Java")

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, coroutineScope)
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = username,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_density_medium_24),
                                contentDescription = "Edit Profile",
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Centered Profile Image
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileImage(
                                imageUri = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                            )
                        }
                    }

                    item {
                        // User Question Stats
                        QuestionStats(
                            modifier = Modifier.fillMaxWidth(),
                            username = username,
                            vm = vm
                        )
                    }

                    item {
                        // Display Name
                        Text(
                            text = userResponse?.name ?: "Name",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        // Social Links Card
                        SocialLinks(
                            modifier = Modifier.fillMaxWidth(),
                            username_LC = username
                        )
                    }

                    item {
                        // Last Seven Days Streak
                        LastSevenDaysStreak(
                            modifier = Modifier.fillMaxWidth(),
                            username = username,
                            vm = vm
                        )
                    }

                    item {
                        // Detailed Stats Card
                        UserStats(
                            modifier = Modifier.fillMaxWidth(),
                            selectedLanguage = selectedLanguage,
                            username = username,
                            vm = vm
                        )
                    }
                }
            },
            bottomBar = {
                BottomNavBar(modifier = Modifier.fillMaxWidth(), navController = navController)
            }
        )
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        DrawerItem(
            label = "Profile",
            onClick = { navController.navigate(Routes.Profile.route) },
            drawerState = drawerState,
            coroutineScope = coroutineScope
        )
        DrawerItem(
            label = "Logout",
            onClick = {
                try {
                    SharedPreferencesManager.clearUserData(navController.context)
                    navController.navigate(Routes.Login.route)
                } catch (e: Exception) {
                    Log.e("DrawerContent", "Error during logout: ${e.localizedMessage}")
                }
            },
            drawerState = drawerState,
            coroutineScope = coroutineScope,
            labelColor = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun DrawerItem(
    label: String,
    onClick: () -> Unit,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    labelColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium,
            color = labelColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch { drawerState.close() }
                onClick()
            }
            .padding(vertical = 12.dp)
    )
}

@Composable
fun QuestionStats(
    modifier: Modifier = Modifier,
    username: String,
    vm: ViewModel,
) {
    var questionsSolved by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        try {
            questionsSolved = vm.questionsSolved(username)
            Log.d("QuestionStats", "$questionsSolved")
        } catch (e: Exception) {
            Log.e("QuestionStats", "Error fetching question stats: ${e.localizedMessage}")
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            number = questionsSolved.getOrNull(1) ?: "0",
            tag = "Easy",
            color = Color(0xFF40BD45)
        )
        StatItem(
            number = questionsSolved.getOrNull(2) ?: "0",
            tag = "Medium",
            color = Color(0xFFC98F1B)
        )
        StatItem(
            number = questionsSolved.getOrNull(3) ?: "0",
            tag = "Hard",
            color = Color(0xFF970300)
        )
    }
}

@Composable
fun UserStats(
    modifier: Modifier,
    selectedLanguage: String,
    username: String,
    vm: ViewModel,
) {
    var questionsSolved by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        try {
            questionsSolved = vm.questionsSolved(username)
            Log.d("UserStats", "$questionsSolved")
        } catch (e: Exception) {
            Log.e("UserStats", "Error fetching user stats: ${e.localizedMessage}")
        }
    }
    Card(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Stats",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            StatsRow(label = "DSA Language:", value = selectedLanguage)
            StatsRow(label = "Questions Solved:", value = questionsSolved.getOrNull(0) ?: "0")
            StatsRow(label = "Leetcode Rank:", value = questionsSolved.getOrNull(4) ?: "0")
            StatsRow(label = "Acceptance Rate:", value = questionsSolved.getOrNull(5) ?: "0")
        }
    }
}

@Composable
fun StatsRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        )
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
fun SocialLinks(
    modifier: Modifier,
    username_LC: String,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Links -->",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            SocialLinkCard(platform = "Leetcode", url = "www.leetcode.com/u/$username_LC")
            SocialLinkCard(platform = "Linkedin", url = "www.linkedin.com")
            SocialLinkCard(platform = "Github", url = "www.github.com")
        }
    }
}

@Composable
fun SocialLinkCard(
    platform: String,
    url: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF5D5D5D))
    ) {
        SocialLinkItem(platform = platform, url = url)
    }
}

@Composable
fun SocialLinkItem(
    platform: String,
    url: String,
) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = url)
        withStyle(style = SpanStyle(fontSize = 16.sp, color = Color(0xFF003A6D))) {
            append(platform)
        }
        pop()
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    "https://$url"
                } else {
                    url
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
                context.startActivity(intent)
            }
    )
}

@Composable
fun StatItem(
    number: String,
    tag: String,
    color: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
        Text(
            text = tag,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier,
    imageUri: String?,
    contentScale: ContentScale,
) {
    if (imageUri.isNullOrEmpty()) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Default Profile Icon",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "Profile Image",
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
