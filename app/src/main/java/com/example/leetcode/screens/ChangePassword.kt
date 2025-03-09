package com.example.leetcode.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.data.EditPassword
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import com.example.leetcode.utils.PasswordTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    sharedPreferencesManager: SharedPreferencesManager,
    vm: UserViewModel,
    navController: NavController,
) {
    val user = sharedPreferencesManager.getUser()

    val id by remember { mutableStateOf(user?.id ?: "") }
    var password by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmNewPassword by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                label = "Current Password",
                password = password,
                onPasswordChange = { password = it }
            )
            PasswordTextField(
                label = "New Password",
                password = newPassword,
                onPasswordChange = { newPassword = it }
            )
            PasswordTextField(
                label = "Confirm New Password",
                password = confirmNewPassword,
                onPasswordChange = { confirmNewPassword = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Button(
                onClick = {
                    errorMessage = null  // Clear previous errors

                    when {
                        password.isBlank() -> errorMessage = "Please enter the current password."
                        newPassword.length < 6 -> errorMessage = "Password must be at least 6 characters long."
                        newPassword == password -> errorMessage = "New password cannot be the same as the current password."
                        newPassword != confirmNewPassword -> errorMessage = "Passwords do not match."
                        else -> {
                            isLoading = true
                            val data = EditPassword(password, newPassword)
                            vm.editPassword(
                                data,
                                id,
                                onSuccess = {
                                    isLoading = false
                                    sharedPreferencesManager.saveUserPassword(newPassword)
                                    errorMessage = "Password Changed Successfully"  // Show success message
                                    navController.navigate(Routes.Home.route) {
                                        popUpTo(Routes.Home.route) { inclusive = true }
                                    }
                                },
                                onError = { error ->
                                    isLoading = false
                                    errorMessage = when {
                                        "Incorrect current password" in error -> "Incorrect current password. Please try again."
                                        "User not found" in error -> "User not found. Please log in again."
                                        else -> "Failed to change password. Please try again."
                                    }
                                }
                            )
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            )
            {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Change Password", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
