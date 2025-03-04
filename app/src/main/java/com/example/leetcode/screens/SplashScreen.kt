package com.example.leetcode.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.R
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager.getUserFromPreferences
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(
    navController: NavController,
) {
    Scaffold(containerColor = Color.Black, content = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.leetcode),
                contentDescription = "Logo",
                modifier = Modifier.size(300.dp)
            )
        }

            LaunchedEffect(Unit) {
                delay(2000)
                val user = getUserFromPreferences(navController.context)

                if (user != null) {
                    navController.popBackStack()
                    navController.navigate(Routes.Home.route)
                } else {
                    navController.popBackStack()
                    navController.navigate(Routes.Login.route) {}
                }
            }
    })
}
