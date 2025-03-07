package com.example.leetcode.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.leetcode),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(32.dp)
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        delay(1500) // Reduced duration for better UX
        val user = try {
            getUserFromPreferences(navController.context)
        } catch (e: Exception) {
            null
        }

        navController.navigate(
            if (user != null) Routes.Home.route
            else Routes.Login.route
        ) {
            popUpTo(Routes.Splash.route) {
                inclusive = true
            }
        }
    }
}