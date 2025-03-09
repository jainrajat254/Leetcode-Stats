package com.example.leetcode.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leetcode.data.EditDetails
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    vm: UserViewModel,
    sharedPreferences: SharedPreferencesManager,
) {
    val user = sharedPreferences.getUser()

    val id by remember { mutableStateOf(user?.id ?: "") }
    var name by remember { mutableStateOf(user?.name ?: "") }
    var username by remember { mutableStateOf(user?.username ?: "") }
    var selectedYear by remember { mutableStateOf(user?.year ?: "") }
    var selectedLanguage by remember { mutableStateOf(user?.selectedLanguage ?: "") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f) // Making it more compact
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownSelector(
                    label = "Year",
                    options = listOf("First", "Second", "Third", "Fourth", "Graduated"),
                    selectedOption = selectedYear,
                    onOptionSelected = { selectedYear = it }
                )

                DropdownSelector(
                    label = "Preferred Language",
                    options = listOf("Java", "C++"),
                    selectedOption = selectedLanguage,
                    onOptionSelected = { selectedLanguage = it }
                )

                Button(
                    onClick = {
                        val data = EditDetails(name, username, selectedYear, selectedLanguage)
                        vm.userDetails(
                            data,
                            id,
                            onSuccess = {
                                isLoading = false
//                                sharedPreferences.saveUserPassword(newPassword)
                                navController.navigate(Routes.Home.route) {
                                    popUpTo(Routes.Home.route) { inclusive = true }
                                }
                                Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
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
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedOption.ifEmpty { label },
        onValueChange = {},
        readOnly = true, // Prevent manual input
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                }
            )
        }
    }
}
