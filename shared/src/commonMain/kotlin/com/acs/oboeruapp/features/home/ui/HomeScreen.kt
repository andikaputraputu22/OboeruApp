package com.acs.oboeruapp.features.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.features.deck.ui.components.AddBlockBottomSheet
import com.acs.oboeruapp.features.deck.ui.components.AddBlockRow
import com.acs.oboeruapp.features.deck.ui.components.DeckCard
import com.acs.oboeruapp.features.home.HomeViewModel
import com.acs.oboeruapp.features.home.ui.components.LearningSpaceSelector
import com.acs.oboeruapp.features.home.ui.components.UserHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onAddLearningSpaceClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddBlockSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        UserHeader(
            isAuthenticated = false,
            photoUrl = null,
            displayName = null,
            onProfileClick = {},
            onNotificationClick = {},
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        LearningSpaceSelector(
            spaces = uiState.learningSpaces,
            selectedId = uiState.selectedSpaceId,
            onSpaceClick = { space -> viewModel.onSpaceSelected(space.id) },
            onAddClick = onAddLearningSpaceClick,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            uiState.deckHeader?.let { header ->
                item(key = uiState.selectedSpaceId) {
                    DeckCard(
                        header = header,
                        blocks = uiState.deckBlocks,
                        onMenuClick = { /* TODO */ },
                        onBlockClick = { block -> /* TODO: navigate sesuai block.id */ },
                        onAddBlockClick = { /* TODO */ }
                    )
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                AddBlockRow(onClick = { showAddBlockSheet = true })
            }
        }
    }

    if (showAddBlockSheet) {
        AddBlockBottomSheet(
            spaceName = "JLPT N2 Preparation",
            onDismiss = { showAddBlockSheet = false },
            onBlockClick = { block ->
                showAddBlockSheet = false
            }
        )
    }
}

