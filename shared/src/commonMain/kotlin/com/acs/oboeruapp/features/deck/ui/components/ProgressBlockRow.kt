package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.acs.oboeruapp.features.deck.domain.model.DeckBlock

@Composable
fun ProgressBlockRow(
    block: DeckBlock.ProgressBlock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DeckBlockCard(
        block = block,
        onClick = onClick,
        modifier = modifier,
    )
}
