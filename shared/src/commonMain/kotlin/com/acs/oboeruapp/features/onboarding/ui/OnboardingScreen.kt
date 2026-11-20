package com.acs.oboeruapp.features.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.onboarding.ui.components.JourneyIllustration
import com.acs.oboeruapp.features.onboarding.ui.components.LearningSpaceIllustration
import com.acs.oboeruapp.features.onboarding.ui.components.OnboardingTopBar
import com.acs.oboeruapp.features.onboarding.ui.components.ProgressIllustration
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onFinishOnboarding: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val pageCount = 3
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }

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
                top = effectiveTopPadding + 8.dp,
                bottom = effectiveBottomPadding + 20.dp,
            ),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // 1. Top Bar: Dots indicator + Skip/Get Started button
        OnboardingTopBar(
            currentPage = pagerState.currentPage,
            pageCount = pageCount,
            onSkipClick = onFinishOnboarding,
        )

        // 2. Horizontal Pager for 3 Onboarding Slides
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { page ->
            OnboardingPageItem(
                pageIndex = page,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            )
        }

        // 3. Bottom Action Pill Button ("Continue →" / "Get Started")
        val isLastPage = pagerState.currentPage == pageCount - 1
        val buttonText = if (isLastPage) "Get Started" else "Continue →"

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {
                        if (isLastPage) {
                            onFinishOnboarding()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageItem(
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    val (title, subtitle) = when (pageIndex) {
        0 -> "Your Japanese Journey\nStarts Here" to "Make learning Japanese\npart of your everyday life."
        1 -> "Build Your Own\nLearning Space" to "Choose what you want to learn,\nand organize your materials."
        else -> "Make Every Learning\nSession Count" to "Track your progress and\nbuild consistent habits."
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Illustration Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            when (pageIndex) {
                0 -> JourneyIllustration()
                1 -> LearningSpaceIllustration()
                else -> ProgressIllustration()
            }
        }

        // Text Content Section (Centered)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 38.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = subtitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    OboeruTheme {
        OnboardingScreen()
    }
}
