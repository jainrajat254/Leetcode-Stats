package com.example.leetcode.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.leetcode.data.Contest
import com.example.leetcode.data.Socials
import com.example.leetcode.models.UserViewModel
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun AuthNavigationText(
    text: String,
    buttonText: String,
    onClick: () -> Unit,
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
    enabled: Boolean = true,
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
    onPasswordChange: (String) -> Unit,
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
    leadingIconRes: Int,
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
    val lastThirtyDays by produceState(initialValue = emptyList<Boolean>(), username) {
        value = try {
            vm.lastThirtyDays(username).also { Log.d("THIRTY DAYS", "$it") }
        } catch (e: Exception) {
            Log.e("LastThirtyDays", "Error fetching streak data: ${e.localizedMessage}")
            emptyList()
        }
    }

    var expanded by remember { mutableStateOf(false) }

    val daysToShow = if (expanded) lastThirtyDays else lastThirtyDays.takeLast(7)

    val cardHeight by animateDpAsState(
        targetValue = if (expanded) 128.dp else 80.dp,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = ""
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
                                    val indicatorColor =
                                        if (isDone) Color(0xFF65E26A) else Color(0xFFE45D5D)
                                    Indicator(
                                        modifier = Modifier.size(20.dp),
                                        color = indicatorColor
                                    )
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
                            val indicatorColor =
                                if (isDone) Color(0xFF65E26A) else Color(0xFFE45D5D)
                            Indicator(modifier = Modifier.size(20.dp), color = indicatorColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
    val questionsSolved by produceState(initialValue = emptyList<String>(), username) {
        value = try {
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
    primaryLanguage: String = "Java",
    vm: UserViewModel,
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ), // Slower & smoother
        label = "Rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                cameraDistance = 12 * density
            }
    ) {
        // Front Card
        if (rotation <= 90f) {
            FrontCard(
                username = username,
                primaryLanguage = primaryLanguage,
                vm = vm,
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation
                }
            )
        }
        // Back Card (Rotated Correctly)
        else {
            BackCard(
                username = username,
                vm = vm,
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation - 180f // Flip back card correctly
                }
            )
        }
    }
}

@Composable
fun BackCard(username: String, vm: UserViewModel, modifier: Modifier = Modifier) {
    val contestInfo by produceState(
        initialValue = Contest(0, 0, 0.0, 0.0), username
    ) {
        value = try {
            vm.getContestInfo(username)
        } catch (e: Exception) {
            Contest(0, 0, 0.0, 0.0)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 75.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (contestInfo.attendedContestsCount == 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No contests attended",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contest Info",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    )

                    StatsRow(
                        "Total Contests",
                        contestInfo.attendedContestsCount.toString(),
                        R.drawable.baseline_numbers_24
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 0.8.dp
                    )

                    StatsRow(
                        "Rating",
                        contestInfo.rating.roundToInt().toString(),
                        R.drawable.baseline_star_24
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 0.8.dp
                    )

                    StatsRow(
                        "Global Ranking",
                        contestInfo.globalRanking.toString(),
                        R.drawable.baseline_leaderboard_24
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 0.8.dp
                    )

                    StatsRow(
                        "Top Percentage",
                        "${contestInfo.topPercentage}%",
                        R.drawable.baseline_percent_24
                    )
                }
            }
        }
    }
}

@Composable
fun FrontCard(username: String, primaryLanguage: String, vm: UserViewModel, modifier: Modifier) {
    val questionsSolved by produceState(initialValue = emptyList<String>(), username) {
        value = try {
            vm.questionsSolved(username)
        } catch (e: Exception) {
            emptyList()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
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
            )

            StatsRow("Primary Language", primaryLanguage, R.drawable.baseline_code_24)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                "Total Solved",
                questionsSolved.getOrNull(0) ?: "0",
                R.drawable.baseline_check_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                "LeetCode Rank",
                questionsSolved.getOrNull(4) ?: "0",
                R.drawable.baseline_leaderboard_24
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)

            StatsRow(
                "Acceptance Rate",
                String.format(
                    Locale.US,
                    "%.2f%%",
                    (questionsSolved.getOrNull(5)?.toFloatOrNull() ?: 0f)
                ),
                R.drawable.baseline_percent_24
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

        // **Wrap LazyVerticalGrid in a Box with fixed height**
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Adjust height as needed
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 Columns for 2x2 layout
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize() // Fill the Box
            ) {
                item {
                    SocialLinkItem(
                        platform = "LeetCode",
                        url = "https://leetcode.com/u/$username",
                        iconRes = R.drawable.leetcode
                    )
                }
                item {
                    SocialLinkItem(
                        platform = "GitHub",
                        url = socials.githubUrl ?: "https://github.com",
                        iconRes = R.drawable.github
                    )
                }
                item {
                    SocialLinkItem(
                        platform = "LinkedIn",
                        url = socials.linkedinUrl ?: "https://linkedin.com",
                        iconRes = R.drawable.linkedin
                    )
                }
                item {
                    SocialLinkItem(
                        platform = "X",
                        url = socials.twitterUrl ?: "https://x.com",
                        iconRes = R.drawable.x
                    )
                }
            }
        }
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


@Composable
fun GenericDropDownMenu(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    label: String,
    items: List<String>,
    leadingIconRes: Int? = null,
    trailingIconRes: ImageVector = Icons.Default.ArrowDropDown, // Default to ArrowDropDown icon
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = trailingIconRes,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            leadingIcon = leadingIconRes?.let {
                {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item, style = MaterialTheme.typography.bodyLarge)
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageDropDownMenu(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
) {
    GenericDropDownMenu(
        selectedItem = selectedLanguage,
        onItemSelected = onLanguageSelected,
        label = "Programming Language",
        items = listOf("Java", "C++"),
        leadingIconRes = R.drawable.baseline_code_24, // Custom icon for languages
    )
}

@Composable
fun YearDropDownMenu(
    selectedYear: String,
    onYearSelected: (String) -> Unit,
) {
    GenericDropDownMenu(
        selectedItem = selectedYear,
        onItemSelected = onYearSelected,
        label = "Current Year",
        items = listOf(
            "First Year",
            "Second Year",
            "Third Year",
            "Fourth Year",
            "Graduated"
        ),
        leadingIconRes = R.drawable.baseline_edit_calendar_24,
    )
}

@Composable
fun CommonTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = if (selectedIndex == index)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            )
        }
    }
}


@Composable
fun FilterFAB(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Image(
            painter = painterResource(id = R.drawable.baseline_filter_list_alt_24),
            contentDescription = "Filter",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    years: List<String>,
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    showActive: Boolean = false,
    isActive: Boolean = false,
    onActiveChange: (Boolean) -> Unit = { },
    minQuestions: String,
    onMinQuestionsChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Filters", style = MaterialTheme.typography.titleMedium)

            DropdownMenuComponent(
                label = "Year",
                options = years,
                selectedOption = selectedYear,
                onOptionSelected = onYearSelected
            )
            if (showActive) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Status: ${if (isActive) "Active" else "Inactive"}")
                    Switch(checked = isActive, onCheckedChange = onActiveChange)
                }
            }

            OutlinedTextField(
                value = minQuestions,
                onValueChange = { if (it.all { char -> char.isDigit() }) onMinQuestionsChange(it) },
                label = { Text("Min Questions Solved") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(8.dp)
            ) {
                Text(text = selectedOption)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth() // Ensures the dropdown width matches the parent
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetwork != null &&
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
}
