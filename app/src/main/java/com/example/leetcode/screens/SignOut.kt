package com.example.leetcode.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignOut(
    modifier: Modifier = Modifier
) {
    Scaffold (
        containerColor = Color.Black,
        content = { paddingValues ->

        }
    )
}

@Preview(showBackground = true)
@Composable
fun SignOutPreview() {
    SignOut()
}