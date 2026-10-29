package com.acs.oboeruapp.features.deck.domain.repository

import com.acs.oboeruapp.features.deck.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    fun getDeckForSpace(spaceId: String): Flow<Deck>
}

