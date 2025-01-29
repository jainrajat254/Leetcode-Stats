package com.example.leetcode.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.leetcode.R
import com.example.leetcode.data.UserData
import com.example.leetcode.models.ViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.utils.AccountCheck
import com.example.leetcode.utils.CustomButton
import com.example.leetcode.utils.CustomTextField

@Composable
fun RegisterScreen(
    modifier: Modifier,
    navController: NavController,
    vm: ViewModel,
) {
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var selectedLanguage by rememberSaveable {
        mutableStateOf("Java")
    }
    val context = LocalContext.current
    var error: String? by remember { mutableStateOf(null) }

    Scaffold(containerColor = Color.Black,
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Register",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
                Image(
                    painter = painterResource(id = R.drawable.leetcode),
                    contentDescription = "Logo",
                    modifier = modifier
                        .size(100.dp)
                        .padding(top = 20.dp)
                )

                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = modifier,
                    label = "Name",
                )

                LanguageDropDownMenu(
                    modifier = modifier,
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { newLanguage ->
                        selectedLanguage = newLanguage
                    }
                )

                CustomTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = modifier,
                    label = "Username",
                )
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = modifier,
                    label = "Password",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                CustomButton(
                    onClick = {
                        val user = UserData(name, selectedLanguage, username, password)
                        vm.registerUser(
                            user,
                            onSuccess = {
                                Toast.makeText(context,"Registration Successful\nLog in to continue",Toast.LENGTH_SHORT).show()
                                navController.navigate(Routes.Login.route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            },
                            onError = { error = it }
                        )
                    },
                    text = "Register",
                    modifier = modifier
                        .width(160.dp)
                        .height(60.dp)
                        .padding(top = 20.dp)
                )

                Spacer(modifier = modifier.height(20.dp))

                AccountCheck(
                    text = "Already have an account? ",
                    check = "Login",
                    navController = navController,
                    navigateTo = Routes.Login.route,
                )
            }
        })
}

@Composable
fun LanguageDropDownMenu(
    modifier: Modifier,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = { },
            label = {
                Text(
                    text = "Language",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    ),
                    color = Color.Gray
                )
            },
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .width(300.dp)
                .padding(top = 10.dp)
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Java") },
                onClick = {
                    onLanguageSelected("Java")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("C++") },
                onClick = {
                    onLanguageSelected("C++")
                    expanded = false
                }
            )
        }
    }
}
