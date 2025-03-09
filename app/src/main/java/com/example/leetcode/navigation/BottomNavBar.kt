package com.example.leetcode.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.leetcode.R
import com.example.leetcode.data.BottomNavItem
import com.example.leetcode.routes.Routes

@Composable
fun BottomNavBar(
    navController: NavController,
) {
    val items = listOf(
        BottomNavItem(Routes.Home.route, R.drawable.baseline_home_24, "Home"),
        BottomNavItem(Routes.Stats.route, R.drawable.baseline_auto_graph_24, "Stats"),
        BottomNavItem(Routes.Leaderboard.route, R.drawable.baseline_leaderboard_24, "Leaderboard"),
        BottomNavItem(Routes.Profile.route, R.drawable.baseline_person_24, "Profile")
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp), // Adjusted for better usability across devices
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), // Increased visibility in both modes
        shadowElevation = 6.dp // Reduced to avoid double shadow effect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Ensures correct placement on all devices
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                BottomNavBarItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(Routes.Home.route) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavBarItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else Color.Transparent)
                .padding(6.dp),
            colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = item.label,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
        )
    }
}