package com.example.leetcode.utils

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.leetcode.R
import com.example.leetcode.models.ViewModel

@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
) {
    OutlinedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(Color(0xFFC98F1B)),
        elevation = ButtonDefaults.buttonElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
    }
}

@Composable
fun AccountCheck(
    text: String,
    check: String,
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateTo: String,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            ),
            color = Color.Gray
        )
        Text(
            text = check,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            ),
            color = Color(0xFFC98F1B),
            modifier = modifier.clickable {
                navController.navigate(navigateTo) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun LastSevenDaysStreak(
    modifier: Modifier,
    username: String,
    vm: ViewModel,
) {
    var lastSevenDays by remember { mutableStateOf<List<Boolean>>(emptyList()) }
    LaunchedEffect(username) {
        try {
            lastSevenDays = vm.lastSevenDays(username)
            Log.d("SEVEN DAYS", "$lastSevenDays")
        } catch (e: Exception) {
            Log.e("StudentStreak", "Error fetching streak data: ${e.localizedMessage}")
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141A))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Last 7 days -->",
                style = TextStyle(
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                lastSevenDays.forEach { isDone ->
                    val indicatorColor = if (isDone) Color(0xFF00FF00) else Color(0xFFFF4444)
                    Indicator(modifier = Modifier.size(20.dp), color = indicatorColor)
                }
            }
        }
    }
}

@Composable
fun Indicator(
    modifier: Modifier,
    color: Color,
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .background(color, shape = RoundedCornerShape(4.dp))
    )
}

@Composable
fun TabContent(
    modifier: Modifier,
    tabs: List<String>,
    contentForTab: (String) -> @Composable () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(tabs.first()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        tabs.forEach { tab ->
            TabItem(
                label = tab,
                isSelected = selectedTab == tab,
                onClick = { selectedTab = tab },
                modifier = modifier.weight(1f)
            )
        }
    }
    val content = contentForTab(selectedTab)
    content()
}

@Composable
fun CustomTopBar(
    modifier: Modifier,
    text: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "Logo",
            modifier = modifier.size(40.dp)
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = text,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.W500
            ),
            color = Color(0xFFC98F1B)
        )
    }
}

@Composable
fun TabItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Text(
        text = label,
        color = if (isSelected) Color.White else Color.Gray,
        modifier = modifier
            .padding(8.dp)
            .background(if (isSelected) Color(0xFF1FD2FA) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    singleLine: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = RoundedCornerShape(16.dp),
    maxLines: Int = 1,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Black,
        unfocusedContainerColor = Color.Black,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedIndicatorColor = Color(0xFFC98F1B),
        unfocusedIndicatorColor = Color.Gray,
        cursorColor = Color(0xFFC98F1B),
        errorCursorColor = Color.Red,
        errorIndicatorColor = Color.Red,
        errorLabelColor = Color.Red
    ),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .width(300.dp)
            .padding(top = 10.dp),
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                ),
                color = Color.Gray
            )
        },
        shape = shape,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        colors = colors
    )
}
