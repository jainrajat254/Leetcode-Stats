package com.example.leetcode.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.leetcode.R
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import com.example.leetcode.utils.AuthButton
import com.example.leetcode.utils.AuthNavigationText
import com.example.leetcode.utils.CustomTextField
import com.example.leetcode.utils.HeaderSection
import com.example.leetcode.utils.PasswordTextField

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: UserViewModel,
) {
    val context = LocalContext.current

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isFormValid by derivedStateOf { username.isNotBlank() && password.isNotBlank() }

    // Ensure SharedPreferences is initialized
    LaunchedEffect(Unit) {
        SharedPreferencesManager.init(context)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection(
                title = "Welcome Back",
                subtitle = "Please sign in to continue"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    leadingIconRes = R.drawable.baseline_person_24
                )

                PasswordTextField(
                    label = "Password",
                    password = password,
                    onPasswordChange = { password = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = "Sign In",
                enabled = isFormValid,
                onClick = {
                    val user = LoginCredentials(username.trim(), password.trim())
                    vm.loginUser(
                        user,
                        onSuccess = { userResponse ->
                            SharedPreferencesManager.saveUser(userResponse)
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Login.route) { inclusive = true }
                            }
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            )

            AuthNavigationText(
                text = "Don't have an account? ",
                buttonText = "Register Now",
                onClick = { navController.navigate(Routes.Register.route) }
            )
        }
    }
}