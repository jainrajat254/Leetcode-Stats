package com.example.leetcode.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.R
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.navigation.BottomNavBar
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import com.example.leetcode.sharedPreferences.SharedPreferencesManager.getUser
import com.example.leetcode.utils.LastThirtyDays
import com.example.leetcode.utils.ProfileHeaderSection
import com.example.leetcode.utils.QuestionStatsSection
import com.example.leetcode.utils.SocialLinksSection
import com.example.leetcode.utils.UserStatsSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    vm: UserViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) { vm.updateAll() }

    val userResponse = getUser()
    val displayName = userResponse?.name ?: "User Name"
    val username = userResponse?.username ?: "username"
    val selectedLanguage by rememberUpdatedState(userResponse?.selectedLanguage ?: "Java")

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            EnhancedDrawerContent(
                navController = navController,
                displayName = displayName,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
                    bottomBar = { BottomNavBar(navController = navController) },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = 20.dp)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { ProfileHeaderSection(displayName = displayName, vm = vm, username = username) }
                    item { QuestionStatsSection(username, vm) }
                    item { LastThirtyDays(username = username, vm = vm, modifier = Modifier) }
                    item {
                        UserStatsSection(
                            primaryLanguage = selectedLanguage,
                            username = username,
                            vm = vm
                        )
                    }
                    item { SocialLinksSection(username, vm) }
                }
            }
        )
    }
}

@Composable
private fun EnhancedDrawerContent(
    navController: NavController,
    displayName: String,
    closeDrawer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp, top = 48.dp, bottom = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            DrawerItem(
                label = "Edit Profile",
                iconRes = R.drawable.baseline_person_24,
                onClick = {
                    closeDrawer()
                    navController.navigate(Routes.EditProfileScreen.route)
                }
            )

            DrawerItem(
                label = "Change Password",
                iconRes = R.drawable.baseline_lock_24,
                onClick = {
                    closeDrawer()
                    navController.navigate(Routes.ChangePassword.route)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 0.8.dp
            )

            DrawerItem(
                label = "Log Out",
                iconRes = R.drawable.baseline_logout_24,
                labelColor = MaterialTheme.colorScheme.error,
                onClick = {
                    SharedPreferencesManager.clearUserData()
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }
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
