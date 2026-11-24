package com.acs.oboeruapp.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.deck.domain.model.DailySummary
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock
import com.acs.oboeruapp.features.deck.ui.components.DeckCard
import com.acs.oboeruapp.features.home.HomeViewModel
import com.acs.oboeruapp.features.home.ui.components.DailySummaryBanner
import com.acs.oboeruapp.features.home.ui.components.UserHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onProfileClick: () -> Unit = {},
    onBlockClick: (UserDeckBlock) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        topBarPadding = topBarPadding,
        bottomBarPadding = bottomBarPadding,
        onProfileClick = onProfileClick,
        onBlockClick = onBlockClick,
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onProfileClick: () -> Unit = {},
    onBlockClick: (UserDeckBlock) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = topBarPadding + 16.dp,
            end = 16.dp,
            bottom = bottomBarPadding + 16.dp,
        )
    ) {
        item(key = "user_header") {
            UserHeader(
                isAuthenticated = uiState.isAuthenticated,
                photoUrl = uiState.photoUrl,
                displayName = uiState.displayName,
                onProfileClick = onProfileClick,
                onNotificationClick = {},
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }

        if (uiState.dailySummary.totalGoals > 0 || uiState.dailySummary.streakDays > 0) {
            item(key = "daily_summary") {
                DailySummaryBanner(
                    summary = uiState.dailySummary,
                    modifier = Modifier.padding(bottom = 22.dp),
                )
            }
        }

        if (!uiState.isLoading && uiState.deckBlocks.isEmpty()) {
            item(key = "empty_blocks") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp, horizontal = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No learning blocks yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Tap the + button below to add your first Japanese study block.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        } else if (uiState.deckBlocks.isNotEmpty()) {
            item(key = "deck_blocks_header") {
                Text(
                    text = "Study Blocks",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    ),
                    modifier = Modifier.padding(start = 6.dp, bottom = 10.dp),
                )
            }

            item(key = "deck_blocks") {
                DeckCard(
                    blocks = uiState.deckBlocks,
                    onBlockClick = onBlockClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    OboeruTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                isLoading = false,
                isAuthenticated = true,
                displayName = "Oboeru",
                dailySummary = DailySummary(completedGoals = 1, totalGoals = 2, streakDays = 4),
                deckBlocks = listOf(
                    UserDeckBlock(
                        id = "1",
                        topic = DeckTopic.HIRAGANA,
                        activityType = DeckActivityType.CHART,
                        dailyTarget = 0,
                        sortOrder = 0,
                    ),
                    UserDeckBlock(
                        id = "2",
                        topic = DeckTopic.HIRAGANA,
                        activityType = DeckActivityType.FLASHCARD,
                        dailyTarget = 15,
                        sortOrder = 1,
                        completedToday = 15,
                    ),
                    UserDeckBlock(
                        id = "3",
                        topic = DeckTopic.HIRAGANA,
                        activityType = DeckActivityType.QUIZ,
                        dailyTarget = 10,
                        sortOrder = 2,
                        completedToday = 4,
                    ),
                    UserDeckBlock(
                        id = "4",
                        topic = DeckTopic.ALL_REVIEW,
                        activityType = DeckActivityType.REVIEW,
                        dailyTarget = 0,
                        sortOrder = 3,
                        dueCount = 12,
                    ),
                ),
            ),
        )
    }
}
