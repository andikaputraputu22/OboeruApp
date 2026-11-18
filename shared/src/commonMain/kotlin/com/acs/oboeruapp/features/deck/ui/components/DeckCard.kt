package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.features.deck.domain.model.DeckBlock

@Composable
fun DeckCard(
    blocks: List<DeckBlock>,
    onBlockClick: (DeckBlock) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        blocks.forEach { block ->
            DeckBlockCard(
                block = block,
                onClick = { onBlockClick(block) },
            )
        }
    }
}
