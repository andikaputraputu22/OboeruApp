package com.acs.oboeruapp.features.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.auth.AuthViewModel
import com.acs.oboeruapp.features.onboarding.ui.components.AppleSignInButton
import com.acs.oboeruapp.features.onboarding.ui.components.GoogleSignInButton
import com.acs.oboeruapp.features.onboarding.ui.components.OrDivider
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onAppleSignInClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onLoginSuccess()
        }
    }

    LandingScreenContent(
        modifier = modifier,
        topBarPadding = topBarPadding,
        bottomBarPadding = bottomBarPadding,
        isSigningIn = uiState.isSigningIn,
        errorMessage = uiState.errorMessage,
        onAppleSignInClick = onAppleSignInClick,
        onGoogleSignInClick = viewModel::signInWithGoogle,
    )
}

@Composable
private fun LandingScreenContent(
    modifier: Modifier = Modifier,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    isSigningIn: Boolean = false,
    errorMessage: String? = null,
    onAppleSignInClick: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = effectiveTopPadding,
                bottom = effectiveBottomPadding + 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Upper / Center Area: Prominent Kanji Logo ("覚") + Brand Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "覚",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 76.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Oboeru",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your Japanese Learning Journey.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Bottom Area: Login Action Buttons (Apple, OR, Google) + Terms/Privacy Disclaimer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                )
            }

            // 1. Apple Sign In Button
            AppleSignInButton(
                onClick = onAppleSignInClick,
                enabled = !isSigningIn,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // OR Divider
            OrDivider()

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Google Sign In Button
            GoogleSignInButton(
                onClick = onGoogleSignInClick,
                enabled = !isSigningIn,
                isLoading = isSigningIn,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Terms & Privacy Note
            val termsText = buildAnnotatedString {
                append("By continuing, you agree to the ")
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline,
                    ),
                ) {
                    append("Terms of Use")
                }
                append(" and acknowledge the ")
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline,
                    ),
                ) {
                    append("Privacy Policy")
                }
                append(" provided by Oboeru.")
            }

            Text(
                text = termsText,
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LandingScreenPreview() {
    OboeruTheme {
        LandingScreenContent()
    }
}
