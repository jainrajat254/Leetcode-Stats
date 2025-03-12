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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.leetcode.R
import com.example.leetcode.data.EditDetails
import com.example.leetcode.models.UserViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import com.example.leetcode.utils.CustomTextField
import com.example.leetcode.utils.LanguageDropDownMenu
import com.example.leetcode.utils.YearDropDownMenu

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
    var selectedYear by remember { mutableStateOf(user?.year ?: "Java") }
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
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    leadingIconRes = R.drawable.baseline_person_24
                )

                CustomTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "username",
                    leadingIconRes = R.drawable.baseline_alternate_email_24
                )

                YearDropDownMenu(
                    selectedYear = selectedYear,
                    onYearSelected = { selectedYear = it }
                )

                LanguageDropDownMenu(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { selectedLanguage = it }
                )

                Button(
                    onClick = {
                        val data = EditDetails(name, username, selectedYear, selectedLanguage)
                        vm.editDetails(
                            data = data,
                            userId = id,
                            onSuccess = {
                                isLoading = false
                                sharedPreferences.saveUserDetails(data)
                                navController.navigate(Routes.Home.route) {
                                    popUpTo(Routes.Home.route) { inclusive = false }
                                }
                                Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT)
                                    .show()
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
