package com.example.leetcode.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier,
    navController: NavController,
) {

    val items = listOf(
        BottomNavItem(Routes.Home.route, R.drawable.baseline_home_24, "Home"),
        BottomNavItem(Routes.Stats.route, R.drawable.baseline_auto_graph_24, "Stats"),
        BottomNavItem(Routes.Leaderboard.route, R.drawable.baseline_leaderboard_24, "Leaderboard"),
        BottomNavItem(Routes.Profile.route, R.drawable.baseline_person_24, "Profile")
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xF1AD7118)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { item ->
            BottomNavBarItem(
                item = item,
                isSelected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = false
                        }
                    }
                }
            )
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
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.LightGray else Color.Transparent)
        )
        Text(
            text = item.label,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 12.sp
        )
    }
}
