package com.acs.oboeruapp.features.deck.domain.model

data class Deck(
    val header: DeckHeader,
    val blocks: List<DeckBlock>
)

