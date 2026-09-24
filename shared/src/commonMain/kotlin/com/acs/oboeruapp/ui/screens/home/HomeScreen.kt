package com.acs.oboeruapp.ui.screens.home

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.data.model.DeckBlock
import com.acs.oboeruapp.data.model.DeckHeader
import com.acs.oboeruapp.ui.components.AddBlockRow
import com.acs.oboeruapp.ui.components.DeckCard
import com.acs.oboeruapp.ui.components.LearningSpaceSelector
import com.acs.oboeruapp.ui.components.UserHeader
import com.acs.oboeruapp.utils.dummyLearningSpaces
import com.acs.oboeruapp.viewmodel.HomeViewModel
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_grammar
import oboeruapp.shared.generated.resources.ic_kanji
import oboeruapp.shared.generated.resources.ic_timer
import oboeruapp.shared.generated.resources.ic_vocabulary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            onAddClick = { /* TODO: navigate ke create learning space */ },
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
                AddBlockRow(onClick = { /* TODO */ })
            }
        }
    }
}