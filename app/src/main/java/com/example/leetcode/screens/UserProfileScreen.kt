package com.example.leetcode.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    vm: ViewModel,
    navController: NavController,
) {
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
            containerColor = Color.Black,
            content = { paddingValues ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = username,
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.baseline_density_medium_24),
                                contentDescription = "EDIT",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(28.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    }
                            )
                        }
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
                            text = userResponse?.name ?: "Name",
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
                            selectedLanguage = selectedLanguage,
                            username = username,
                            vm = vm
                        )
                    }
                }
            },
            bottomBar = { BottomNavBar(modifier = modifier, navController = navController) }
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
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 28.dp)

    ) {
        Text(
            text = "Menu",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        DrawerItem(
            label = "Profile",
            onClick = { navController.navigate(Routes.Profile.route) },
            drawerState,
            coroutineScope
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
            drawerState,
            coroutineScope,
            labelColor = Color.Red
        )
    }
}

@Composable
fun DrawerItem(
    label: String,
    onClick: () -> Unit,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    labelColor: Color = Color.White
) {
    Text(
        text = label,
        style = TextStyle(
            fontSize = 20.sp,
            color = labelColor,
            fontWeight = FontWeight.W600
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable {
                try {
                    coroutineScope.launch { drawerState.close() }
                    onClick()
                } catch (e: Exception) {
                    Log.e("DrawerItem", "Error navigating to $label: ${e.localizedMessage}")
                }
            }
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
            Log.d("Questions Solved", "$questionsSolved")
        } catch (e: Exception) {
            Log.e("QuestionStats", "Error fetching question stats: ${e.localizedMessage}")
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 40.dp, top = 32.dp)
        ) {
            StatItem(
                number = questionsSolved.getOrNull(1) ?: "0",
                tag = "Easy",
                color = Color(0xFF40BD45)
            )
            Spacer(modifier = Modifier.width(20.dp))

            StatItem(
                number = questionsSolved.getOrNull(2) ?: "0",
                tag = "Medium",
                color = Color(0xFFC98F1B)
            )
            Spacer(modifier = Modifier.width(20.dp))

            StatItem(
                number = questionsSolved.getOrNull(3) ?: "0",
                tag = "Hard",
                color = Color(0xFF970300)
            )
        }
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
            Log.d("Questions Solved", "$questionsSolved")
        } catch (e: Exception) {
            Log.e("UserStats", "Error fetching user stats: ${e.localizedMessage}")
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141A))
    ) {
        Text(
            text = "Stats",
            style = TextStyle(
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
            ),
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 12.dp)
        )

        StatsRow(label = "DSA Language : ", value = selectedLanguage, modifier = modifier)
        StatsRow(
            label = "Questions Solved : ",
            value = questionsSolved.getOrNull(0) ?: "0",
            modifier = modifier
        )
        StatsRow(
            label = "Leetcode Rank : ",
            value = questionsSolved.getOrNull(4) ?: "0",
            modifier = modifier
        )
        StatsRow(
            label = "Acceptance Rate",
            value = questionsSolved.getOrNull(5) ?: "0",
            modifier = modifier
        )
    }
}

@Composable
fun StatsRow(
    label: String,
    value: String,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 24.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = Color(0xC7FFFFFF),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = value,
            style = TextStyle(
                color = Color(0xC7FFFFFF),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
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
            .fillMaxWidth()
            .wrapContentSize()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141A))
    ) {
        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Links -->",
                style = TextStyle(
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                ),
                modifier = modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp)
            )

            Spacer(modifier = modifier.height(8.dp))

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(horizontal = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF5D5D5D))
            ) {
                SocialLinkItem(
                    platform = "Leetcode",
                    url = "www.leetcode.com/u/$username_LC",
                    modifier = modifier
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(horizontal = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF5D5D5D))
            ) {
                SocialLinkItem(
                    platform = "Linkedin",
                    url = "www.linkedin.com",
                    modifier = modifier
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(horizontal = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF5D5D5D))
            ) {
                SocialLinkItem(
                    platform = "Github",
                    url = "www.github.com",
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun SocialLinkItem(
    platform: String,
    url: String,
    modifier: Modifier,
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
        style = TextStyle(fontSize = 20.sp, letterSpacing = 1.sp),
        modifier = modifier
            .padding(top = 2.dp, start = 8.dp)
            .clickable {
                val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    "https://$url"
                } else {
                    url
                }
                val annotations = annotatedString.getStringAnnotations(
                    tag = "URL",
                    start = 0,
                    end = annotatedString.length
                )
                if (annotations.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
                    context.startActivity(intent)
                }
            },
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
    ) {
        Text(
            text = number,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
        Text(
            text = tag,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
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
            contentDescription = "Default Icon",
            tint = Color.Gray,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "Profile",
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

