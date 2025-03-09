package com.example.leetcode.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.leetcode.R
import com.example.leetcode.data.UserData
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.AuthButton
import com.example.leetcode.utils.AuthNavigationText
import com.example.leetcode.utils.CustomTextField
import com.example.leetcode.utils.HeaderSection
import com.example.leetcode.utils.LanguageDropDownMenu
import com.example.leetcode.utils.PasswordTextField
import com.example.leetcode.utils.YearDropDownMenu

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: UserViewModel,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("First Year") }
    var selectedLanguage by rememberSaveable { mutableStateOf("Java") }
    val context = LocalContext.current
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val errorMessage by vm.errorMessage.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderSection(title = "Create Account")
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        leadingIconRes = R.drawable.baseline_person_24
                    )

                    LanguageDropDownMenu(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    YearDropDownMenu(
                        selectedYear = year,
                        onYearSelected = { year = it }
                    )

                    CustomTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Username",
                        leadingIconRes = R.drawable.baseline_alternate_email_24
                    )

                    PasswordTextField(
                        label = "Password",
                        password = password,
                        onPasswordChange = { password = it }
                    )

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AuthButton(
                    text = if (isLoading) "Registering..." else "Register",
                    onClick = {
                        if (name.isBlank() || username.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            val user = UserData(name, selectedLanguage, year, username, password)
                            vm.registerUser(
                                user,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Registration Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(Routes.Home.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    },
                    enabled = !isLoading
                )

                AuthNavigationText(
                    text = "Already have an account? ",
                    buttonText = "Sign In",
                    onClick = { navController.navigate(Routes.Login.route) }
                )
            }
        }
    )
}
