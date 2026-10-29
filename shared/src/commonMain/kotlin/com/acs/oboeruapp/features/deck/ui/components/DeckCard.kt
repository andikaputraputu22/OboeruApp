package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        DeckHeaderCard(header = header, onMenuClick = onMenuClick)

        blocks.forEachIndexed { index, block ->
            when (block) {
                is DeckBlock.ProgressBlock -> ProgressBlockRow(
                    block = block,
                    onClick = { onBlockClick(block) },
                )
                is DeckBlock.SimpleBlock -> SimpleBlockRow(
                    block = block,
                    onClick = { onBlockClick(block) },
                )
            }
            if (index != blocks.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                )
            }
        }
    }
}

