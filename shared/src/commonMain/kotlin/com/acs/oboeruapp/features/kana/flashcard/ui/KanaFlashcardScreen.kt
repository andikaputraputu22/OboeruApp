package com.acs.oboeruapp.features.kana.flashcard.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.core.domain.srs.SrsRating
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.flashcard.FlashcardFilter
import com.acs.oboeruapp.features.kana.flashcard.FlashcardSessionMode
import com.acs.oboeruapp.features.kana.flashcard.KanaFlashcardViewModel
import com.acs.oboeruapp.features.kana.flashcard.SrsIntervalHints
import com.acs.oboeruapp.features.kana.flashcard.ui.components.FlashcardSessionComplete
import com.acs.oboeruapp.features.kana.flashcard.ui.components.KanaFlipCard
import com.acs.oboeruapp.features.kana.flashcard.ui.components.SrsRatingBar
import com.acs.oboeruapp.features.profile.ui.components.ProfileTopBar
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun KanaFlashcardScreen(
    script: KanaScript = KanaScript.HIRAGANA,
    blockId: String? = null,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onBackClick: () -> Unit = {},
    viewModel: KanaFlashcardViewModel = koinViewModel(
        key = "kana_flashcard_${script.name}_${blockId ?: "standalone"}",
        parameters = { parametersOf(script, blockId) },
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    KanaFlashcardScreenContent(
        uiState = uiState,
        topBarPadding = topBarPadding,
        bottomBarPadding = bottomBarPadding,
        onBackClick = onBackClick,
        onFilterSelected = viewModel::onFilterSelected,
        onFlipCard = viewModel::onFlipCard,
        onRateCard = viewModel::onRateCard,
        onRestartSession = viewModel::onRestartSession,
    )
}

@Composable
private fun KanaFlashcardScreenContent(
    uiState: KanaFlashcardUiState,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onBackClick: () -> Unit = {},
    onFilterSelected: (FlashcardFilter) -> Unit = {},
    onFlipCard: () -> Unit = {},
    onRateCard: (SrsRating) -> Unit = {},
    onRestartSession: () -> Unit = {},
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

    val scriptName = if (uiState.script == KanaScript.HIRAGANA) "Hiragana" else "Katakana"
    val scriptAccent = if (uiState.script == KanaScript.HIRAGANA) {
        Color(0xFF38BDF8)
    } else {
        Color(0xFFA78BFA)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progressFraction,
        label = "flashcard_session_progress",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = effectiveTopPadding + 8.dp,
                bottom = effectiveBottomPadding + 16.dp,
            ),
    ) {
        val isFreePractice = uiState.sessionMode == FlashcardSessionMode.FREE_PRACTICE
        val badgeAccent = if (isFreePractice || uiState.isSessionComplete) {
            Color(0xFF4ADE80)
        } else {
            scriptAccent
        }
        val modeLabel = when (uiState.sessionMode) {
            FlashcardSessionMode.DAILY_GOAL -> "DAILY GOAL"
            FlashcardSessionMode.FREE_PRACTICE -> "FREE PRACTICE"
            FlashcardSessionMode.STANDALONE -> "PRACTICE SESSION"
        }
        val counterText = when {
            uiState.isLoading -> null
            isFreePractice -> "✓ ${uiState.completedCards} today"
            uiState.totalCards > 0 -> "${uiState.completedCards} / ${uiState.totalCards}"
            else -> null
        }

        // Top Bar (centered title, consistent with ProfileScreen & KanaChartScreen)
        ProfileTopBar(
            title = "$scriptName Flashcards",
            onBackClick = onBackClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))

        // Mode Label + Counter + Progress Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            if (counterText != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = modeLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = counterText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeAccent,
                    )
                }

                Spacer(Modifier.height(7.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
            ) {
                if (animatedProgress > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(badgeAccent),
                    )
                }
            }
        }

        if (!uiState.isSessionComplete) {
            Spacer(Modifier.height(14.dp))

            FlashcardFilterRow(
                selectedFilter = uiState.selectedFilter,
                accentColor = scriptAccent,
                onSelectFilter = onFilterSelected,
            )
        }

        Spacer(Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Spacer(Modifier.weight(1f))
            }

            uiState.isSessionComplete -> {
                val reviewedCount = uiState.sessionReviewedCards
                val unit = if (reviewedCount == 1) "card" else "cards"
                val completionTitle = if (uiState.tracksFullDailyGoal) {
                    "Daily Goal Complete!"
                } else {
                    "Session Complete!"
                }
                val completionSubtitle = if (uiState.tracksFullDailyGoal) {
                    "You reached your daily goal of ${uiState.totalCards} $scriptName cards"
                } else {
                    "You reviewed $reviewedCount $scriptName $unit"
                }
                val primaryButtonText = if (uiState.tracksFullDailyGoal) {
                    "Free Practice"
                } else {
                    "Practice Again"
                }
                FlashcardSessionComplete(
                    title = completionTitle,
                    subtitle = completionSubtitle,
                    goodCount = uiState.goodCount,
                    easyCount = uiState.easyCount,
                    againCount = uiState.againCount,
                    accuracyPercent = uiState.accuracyPercent,
                    primaryButtonText = primaryButtonText,
                    onPrimaryClick = onRestartSession,
                    onDoneClick = onBackClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                )
            }

            uiState.currentCard != null -> {
                val activeCard = uiState.currentCard
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedContent(
                        targetState = uiState.cardTransitionKey,
                        transitionSpec = {
                            (slideInHorizontally(animationSpec = tween(260)) { it / 4 } + fadeIn(tween(220))) togetherWith
                                (slideOutHorizontally(animationSpec = tween(220)) { -it / 4 } + fadeOut(tween(180)))
                        },
                        label = "flashcard_card_transition",
                    ) { transitionKey ->
                        val displayedCard = remember(transitionKey) { activeCard }
                        var displayedFlipped by remember(transitionKey) { mutableStateOf(false) }
                        if (transitionKey == uiState.cardTransitionKey) {
                            displayedFlipped = uiState.isFlipped
                        }
                        KanaFlipCard(
                            itemWithProgress = displayedCard,
                            isFlipped = displayedFlipped,
                            accentColor = scriptAccent,
                            onFlip = {
                                if (transitionKey == uiState.cardTransitionKey) {
                                    onFlipCard()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                                .fillMaxHeight(),
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                SrsRatingBar(
                    isFlipped = uiState.isFlipped,
                    isSubmitting = uiState.isSubmittingRating,
                    intervalHints = uiState.intervalHints,
                    goodAccentColor = scriptAccent,
                    onRevealClick = onFlipCard,
                    onRateClick = onRateCard,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun FlashcardFilterRow(
    selectedFilter: FlashcardFilter,
    accentColor: Color,
    onSelectFilter: (FlashcardFilter) -> Unit,
) {
    val filters = remember { FlashcardFilter.entries }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            accentColor.copy(alpha = 0.16f)
                        } else {
                            Color.White.copy(alpha = 0.05f)
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) accentColor.copy(alpha = 0.45f) else Color.Transparent,
                        shape = CircleShape,
                    )
                    .clickable { onSelectFilter(filter) }
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = filter.label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun KanaFlashcardFrontPreview() {
    val sampleCard = KanaItemWithProgress(
        item = KanaItem(
            id = "hiragana_a",
            script = KanaScript.HIRAGANA,
            category = KanaCategory.BASIC,
            groupKey = "a",
            character = "あ",
            romaji = "a",
            exampleWord = "あめ (ame)",
            exampleMeaning = "Rain",
            rowIndex = 0,
            colIndex = 0,
            sortOrder = 0,
        ),
        status = ProgressStatus.LEARNING,
    )

    OboeruTheme {
        KanaFlashcardScreenContent(
            uiState = KanaFlashcardUiState(
                isLoading = false,
                script = KanaScript.HIRAGANA,
                currentCard = sampleCard,
                isFlipped = false,
                intervalHints = SrsIntervalHints("10m", "1d", "7d"),
                completedCards = 3,
                totalCards = 10,
            ),
        )
    }
}

@Preview
@Composable
private fun KanaFlashcardBackPreview() {
    val sampleCard = KanaItemWithProgress(
        item = KanaItem(
            id = "hiragana_a",
            script = KanaScript.HIRAGANA,
            category = KanaCategory.BASIC,
            groupKey = "a",
            character = "あ",
            romaji = "a",
            exampleWord = "あめ (ame)",
            exampleMeaning = "Rain",
            rowIndex = 0,
            colIndex = 0,
            sortOrder = 0,
        ),
        status = ProgressStatus.LEARNING,
        correctCount = 2,
        wrongCount = 1,
    )

    OboeruTheme {
        KanaFlashcardScreenContent(
            uiState = KanaFlashcardUiState(
                isLoading = false,
                script = KanaScript.HIRAGANA,
                currentCard = sampleCard,
                isFlipped = true,
                intervalHints = SrsIntervalHints("10m", "1d", "7d"),
                completedCards = 3,
                totalCards = 10,
            ),
        )
    }
}
