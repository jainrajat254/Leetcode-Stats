package com.example.leetcode.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavHostController
import com.example.leetcode.R
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.models.ViewModel
import com.example.leetcode.routes.Routes
import com.example.leetcode.sharedPreferences.SharedPreferencesManager.saveUserToPreferences
import com.example.leetcode.utils.AccountCheck
import com.example.leetcode.utils.CustomButton
import com.example.leetcode.utils.CustomTextField

@Composable
fun LoginScreen(
    modifier: Modifier,
    navController: NavHostController,
    vm: ViewModel,
) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Scaffold(containerColor = Color.Black, content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back!!",
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
                    val user = LoginCredentials(username, password)
                    vm.loginUser(
                        user,
                        onSuccess = { userResponse ->
                            saveUserToPreferences(context,userResponse)
                            navController.navigate(Routes.Home.route) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                            }
                        },
                        onError = {
                            Toast.makeText(context, "Bad Credentials", Toast.LENGTH_SHORT).show()
                            error("Bad Credentials")
                        }
                    )
                },
                text = "Login",
                modifier = modifier
                    .width(160.dp)
                    .height(60.dp)
                    .padding(top = 20.dp)
            )

            Spacer(modifier = modifier.height(20.dp))

            AccountCheck(
                text = "Don't have an account? ",
                check = "Register",
                navController = navController,
                navigateTo = Routes.Register.route,
            )
        }
    })
}