package com.example.leetcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import com.example.leetcode.navigation.App
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import com.example.leetcode.ui.theme.LeetcodeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesManager.init(this)
        enableEdgeToEdge()
        setContent {
            LeetcodeTheme {
                App()
            }
        }
    }
}
