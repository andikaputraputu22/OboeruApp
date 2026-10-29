package com.acs.oboeruapp.features.deck.data.repository

import com.acs.oboeruapp.features.deck.domain.model.Deck
import com.acs.oboeruapp.features.deck.domain.model.DeckHeader
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.utils.dummyDecks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DeckRepositoryImpl : DeckRepository {

    override fun getDeckForSpace(spaceId: String): Flow<Deck> {
        val deck = dummyDecks[spaceId] ?: Deck(
            header = DeckHeader(
                title = "",
                description = "",
                backgroundImageUrl = ""
            ),
            blocks = emptyList()
        )

        return flowOf(deck)
    }
}

