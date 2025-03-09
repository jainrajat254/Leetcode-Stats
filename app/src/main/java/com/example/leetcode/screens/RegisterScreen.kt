package com.example.leetcode.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.leetcode.utils.*

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: UserViewModel,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
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
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_person_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )

                    LanguageDropDownMenu(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it },
                        modifier = Modifier.fillMaxWidth()
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
                            Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
                        } else {
                            val user = UserData(name, selectedLanguage, username, password)
                            vm.registerUser(
                                user,
                                onSuccess = {
                                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
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

@Composable
fun LanguageDropDownMenu(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = {},
            label = { Text("Programming Language") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Language dropdown",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_code_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            listOf("Java", "C++").forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(text = language, style = MaterialTheme.typography.bodyLarge)
                    },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}