package com.example.leetcode.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.leetcode.models.ViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.screens.HomeScreen
import com.example.leetcode.screens.LeaderboardScreen
import com.example.leetcode.screens.LoginScreen
import com.example.leetcode.screens.ProfileScreen
import com.example.leetcode.screens.RegisterScreen
import com.example.leetcode.screens.SplashScreen
import com.example.leetcode.screens.StatsScreen

@Composable
fun App() {
    val navController = rememberNavController()
    val vm = hiltViewModel<ViewModel>()

    LaunchedEffect(Unit) {
        vm.updateAll()
    }

    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController = navController, vm = vm, modifier = Modifier)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController, vm = vm, modifier = Modifier)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController, vm = vm, modifier = Modifier)
        }
        composable(Routes.Home.route) {
            HomeScreen(navController = navController, vm = vm, modifier = Modifier)
        }
        composable(Routes.Leaderboard.route) {
            LeaderboardScreen(navController = navController, vm = vm, modifier = Modifier)
        }
        composable(Routes.Stats.route) {
            StatsScreen(navController = navController, vm = vm, modifier = Modifier)
        }
//        composable(Routes.Edit.route) {
//            EditScreen(navController = navController)
//        }
    }
}