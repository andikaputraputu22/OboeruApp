package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.features.deck.domain.model.DeckBlock
import com.acs.oboeruapp.features.deck.domain.model.DeckHeader

@Composable
fun DeckCard(
    header: DeckHeader,
    blocks: List<DeckBlock>,
    onMenuClick: () -> Unit,
    onBlockClick: (DeckBlock) -> Unit,
    onAddBlockClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DeckHeaderCard(
            header = header,
            onMenuClick = onMenuClick,
        )

        blocks.forEach { block ->
            DeckBlockCard(
                block = block,
                onClick = { onBlockClick(block) },
            )
        }
    }
}
