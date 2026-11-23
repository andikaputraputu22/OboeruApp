package com.acs.oboeruapp.features.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.profile.ProfileViewModel
import com.acs.oboeruapp.features.profile.ui.components.ProfileItemDivider
import com.acs.oboeruapp.features.profile.ui.components.ProfileItemRow
import com.acs.oboeruapp.features.profile.ui.components.ProfileSection
import com.acs.oboeruapp.features.profile.ui.components.ProfileTopBar
import com.acs.oboeruapp.features.profile.ui.components.UserHeroSection
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_arrow_right
import oboeruapp.shared.generated.resources.ic_database
import oboeruapp.shared.generated.resources.ic_external_link
import oboeruapp.shared.generated.resources.ic_feedback
import oboeruapp.shared.generated.resources.ic_info
import oboeruapp.shared.generated.resources.ic_language
import oboeruapp.shared.generated.resources.ic_logout
import oboeruapp.shared.generated.resources.ic_notification
import oboeruapp.shared.generated.resources.ic_palette
import oboeruapp.shared.generated.resources.ic_person
import oboeruapp.shared.generated.resources.ic_star
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    title: String = "Profile",
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onBackClick: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onAccountInfoClick: () -> Unit = {},
    onDataSyncClick: () -> Unit = {},
    onRateClick: () -> Unit = {},
    onSendFeedbackClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    ProfileScreenContent(
        modifier = modifier,
        userName = uiState.userName,
        userEmail = uiState.userEmail,
        photoUrl = uiState.photoUrl,
        isSigningOut = uiState.isSigningOut,
        errorMessage = uiState.errorMessage,
        title = title,
        topBarPadding = topBarPadding,
        bottomBarPadding = bottomBarPadding,
        onBackClick = onBackClick,
        onAppearanceClick = onAppearanceClick,
        onLanguageClick = onLanguageClick,
        onNotificationsClick = onNotificationsClick,
        onAccountInfoClick = onAccountInfoClick,
        onDataSyncClick = onDataSyncClick,
        onRateClick = onRateClick,
        onSendFeedbackClick = onSendFeedbackClick,
        onAboutClick = onAboutClick,
        onLogoutClick = { showLogoutDialog = true },
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Log out?",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            text = {
                Text(
                    text = "You'll need to sign in again to access your account.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.signOut()
                    }
                ) {
                    Text(
                        text = "Log out",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )
    }
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    userName: String = "Coding Gameplay",
    userEmail: String = "codewithputu@gmail.com",
    photoUrl: String? = null,
    isSigningOut: Boolean = false,
    errorMessage: String? = null,
    title: String = "Profile",
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onBackClick: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onAccountInfoClick: () -> Unit = {},
    onDataSyncClick: () -> Unit = {},
    onRateClick: () -> Unit = {},
    onSendFeedbackClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val effectiveTopPadding = if (topBarPadding > 0.dp) {
        topBarPadding
    } else {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    }

    val effectiveBottomPadding = if (bottomBarPadding > 0.dp) {
        bottomBarPadding
    } else {
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = effectiveTopPadding + 8.dp,
            end = 16.dp,
            bottom = effectiveBottomPadding + 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        // Top Navigation Bar
        item(key = "profile_top_bar") {
            ProfileTopBar(
                title = title,
                onBackClick = onBackClick,
            )
        }

        // Hero Avatar & User Info Section
        item(key = "user_hero") {
            UserHeroSection(
                userName = userName,
                userEmail = userEmail,
                photoUrl = photoUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        }

        // 1. App Section
        item(key = "section_app") {
            ProfileSection(title = "App") {
                ProfileItemRow(
                    title = "Appearance",
                    icon = Res.drawable.ic_palette,
                    onClick = onAppearanceClick,
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
                ProfileItemDivider()
                ProfileItemRow(
                    title = "Language",
                    icon = Res.drawable.ic_language,
                    onClick = onLanguageClick,
                    trailingText = "English",
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
                ProfileItemDivider()
                ProfileItemRow(
                    title = "Notifications",
                    icon = Res.drawable.ic_notification,
                    onClick = onNotificationsClick,
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
            }
        }

        // 2. Account Section
        item(key = "section_account") {
            ProfileSection(title = "Account") {
                ProfileItemRow(
                    title = "Account information",
                    icon = Res.drawable.ic_person,
                    onClick = onAccountInfoClick,
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
                ProfileItemDivider()
                ProfileItemRow(
                    title = "Data & sync",
                    icon = Res.drawable.ic_database,
                    onClick = onDataSyncClick,
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
            }
        }

        // 3. About Section
        item(key = "section_about") {
            ProfileSection(title = "About") {
                ProfileItemRow(
                    title = "Rate Oboeru",
                    icon = Res.drawable.ic_star,
                    onClick = onRateClick,
                    trailingIcon = Res.drawable.ic_external_link,
                )
                ProfileItemDivider()
                ProfileItemRow(
                    title = "Send feedback",
                    icon = Res.drawable.ic_feedback,
                    onClick = onSendFeedbackClick,
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
                ProfileItemDivider()
                ProfileItemRow(
                    title = "About Oboeru",
                    icon = Res.drawable.ic_info,
                    onClick = onAboutClick,
                    trailingText = "v1.0.0",
                    trailingIcon = Res.drawable.ic_arrow_right,
                )
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            item(key = "logout_error") {
                Text(
                    text = errorMessage,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // 4. Logout Section
        item(key = "section_logout") {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.07f)),
            ) {
                ProfileItemRow(
                    title = if (isSigningOut) "Logging out..." else "Log out",
                    icon = Res.drawable.ic_logout,
                    onClick = onLogoutClick,
                    enabled = !isSigningOut,
                    iconTint = MaterialTheme.colorScheme.error,
                    titleColor = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    OboeruTheme {
        ProfileScreenContent()
    }
}
