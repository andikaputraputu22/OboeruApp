package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock

@Composable
fun DeckCard(
    blocks: List<UserDeckBlock>,
    onBlockClick: (UserDeckBlock) -> Unit,
    modifier: Modifier = Modifier,
    onBlockLongClick: ((UserDeckBlock) -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        blocks.forEach { block ->
            DeckBlockCard(
                block = block,
                onClick = { onBlockClick(block) },
                onLongClick = onBlockLongClick?.let { handler -> { handler(block) } },
            )
        }
    }
}
