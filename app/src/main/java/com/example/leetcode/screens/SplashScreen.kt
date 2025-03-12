package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.R
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(vm: UserViewModel, navController: NavController) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background,
        content = { _ ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(32.dp)
                )
            }
        }
    )
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(1500)
        val isLoggedIn = SharedPreferencesManager.isLoggedIn()
        val username = SharedPreferencesManager.getUsername()

        if (!isLoggedIn || username.isNullOrBlank()) {
            navController.navigate(Routes.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        vm.isValidUser(username) { result ->
            val message = result["message"] ?: result["error"]

            if (message == "User Found") {
                navController.navigate(Routes.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                SharedPreferencesManager.clearUserData()
                navController.navigate(Routes.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
