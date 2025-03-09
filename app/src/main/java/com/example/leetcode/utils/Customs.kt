package com.example.leetcode.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.leetcode.R
import com.example.leetcode.data.Socials
import com.example.leetcode.models.UserViewModel

@Composable
fun AuthNavigationText(
    text: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onClick) {
            Text(
                text = buttonText,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled, // Disable button if `enabled` is false
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}


@Composable
fun PasswordTextField(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_lock_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) R.drawable.baseline_visibility_24
                        else R.drawable.baseline_visibility_off_24
                    ),
                    contentDescription = "Toggle password visibility",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIconRes: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = true
    )
}

@Composable
fun HeaderSection(title: String, subtitle: String? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        )

        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun LastThirtyDays(
    modifier: Modifier = Modifier,
    username: String,
    vm: UserViewModel,
) {
    var lastThirtyDays by remember { mutableStateOf<List<Boolean>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(username) {
        try {
            lastThirtyDays = vm.lastThirtyDays(username)
            Log.d("THIRTY DAYS", "$lastThirtyDays")
        } catch (e: Exception) {
            Log.e("LastThirtyDays", "Error fetching streak data: ${e.localizedMessage}")
        }
    }

    val daysToShow = if (expanded) lastThirtyDays else lastThirtyDays.takeLast(7)

    val cardHeight by animateDpAsState(
        targetValue = if (expanded) 128.dp else 80.dp, // Increased size for 30 days
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing), label = ""
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp)
            .clickable { expanded = !expanded } // Toggle expanded on tap
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .animateContentSize()
            ) {
                if (expanded) {
                    // 3 Rows of 10 indicators each for 30 days
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        daysToShow.chunked(10).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowItems.forEach { isDone ->
                                    val indicatorColor = if (isDone) Color(0xFF65E26A) else Color(0xFFE45D5D)
                                    Indicator(modifier = Modifier.size(20.dp), color = indicatorColor)
                                }
                            }
                        }
                    }
                } else {
                    // Row for 7 Days
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        daysToShow.forEach { isDone ->
                            val indicatorColor = if (isDone) Color(0xFF65E26A) else Color(0xFFE45D5D)
                            Indicator(modifier = Modifier.size(20.dp), color = indicatorColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp)) // Adjust spacing

                Text(
                    text = if (expanded) "Last 30 days" else "Last 7 days",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
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
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
fun ProfileHeaderSection(
    username: String = "N/A",
    displayName: String? = null,  // Null means use username
    vm: UserViewModel,
) {
    var profilePhoto by remember { mutableStateOf("") }

    LaunchedEffect(username) {
        profilePhoto = vm.getUserProfile(username).userAvatar ?: ""
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = profilePhoto.ifEmpty { R.drawable.baseline_person_24 },
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.baseline_person_24),
                error = painterResource(R.drawable.baseline_person_24)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = displayName ?: username,  // Show name if available, else username
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = "@$username",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        )
    }
}



@Composable
fun QuestionStatsSection(username: String, vm: UserViewModel) {
    var questionsSolved by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        questionsSolved = try {
            vm.questionsSolved(username)
        } catch (e: Exception) {
            emptyList()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "Problem Solving Stats",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    number = questionsSolved.getOrNull(1) ?: "0",
                    tag = "Easy",
                    color = Color(0xFF00B8A3)
                )
                StatItem(
                    number = questionsSolved.getOrNull(2) ?: "0",
                    tag = "Medium",
                    color = Color(0xFFFFC01E)
                )
                StatItem(
                    number = questionsSolved.getOrNull(3) ?: "0",
                    tag = "Hard",
                    color = Color(0xFFFF375F)
                )
            }
        }
    }
}

@Composable
fun UserStatsSection(
    username: String = "N/A",
    primaryLanguage: String = "Java",  // Default language
    vm: UserViewModel
) {
    var questionsSolved by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(username) {
        questionsSolved = try {
            vm.questionsSolved(username)
        } catch (e: Exception) {
            emptyList()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Performance Overview",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            StatsRow(
                label = "Primary Language",
                value = primaryLanguage,
                iconRes = R.drawable.baseline_code_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                label = "Total Solved",
                value = questionsSolved.getOrNull(0) ?: "0",
                iconRes = R.drawable.baseline_check_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                label = "LeetCode Rank",
                value = questionsSolved.getOrNull(4) ?: "0",
                iconRes = R.drawable.baseline_leaderboard_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                label = "Acceptance Rate",
                value = String.format(
                    "%.2f%%",
                    (questionsSolved.getOrNull(5)?.toFloatOrNull() ?: 0f)
                ),
                iconRes = R.drawable.baseline_percent_24
            )
        }
    }
}



@Composable
fun SocialLinksSection(username: String, vm: UserViewModel) {

    var socials by remember { mutableStateOf(Socials("", "", "", "")) }

    LaunchedEffect(Unit) {
        socials = vm.getUserSocials(username)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Social Profiles",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
        )

        SocialLinkItem(
            platform = "LeetCode",
            url = "https://leetcode.com/u/$username",
            iconRes = R.drawable.leetcode
        )
        SocialLinkItem(
            platform = "GitHub",
            url = socials.githubUrl ?: "https://github.com",
            iconRes = R.drawable.github
        )
        SocialLinkItem(
            platform = "LinkedIn",
            url = socials.linkedinUrl ?: "https://linkedin.com",
            iconRes = R.drawable.linkedin
        )

    }
}

@Composable
fun StatItem(
    number: String,
    tag: String,
    color: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 32.sp
            )
        )
        Text(
            text = tag,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun StatsRow(
    label: String,
    value: String,
    iconRes: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun SocialLinkItem(
    platform: String,
    url: String,
    iconRes: Int,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )
                    )
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = platform,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

