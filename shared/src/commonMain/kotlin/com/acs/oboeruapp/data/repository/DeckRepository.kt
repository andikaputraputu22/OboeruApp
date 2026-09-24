package com.acs.oboeruapp.data.repository

import com.acs.oboeruapp.data.model.Deck
import com.acs.oboeruapp.data.model.DeckHeader
import com.acs.oboeruapp.utils.dummyDecks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface DeckRepository {
    fun getDeckForSpace(spaceId: String): Flow<Deck>
}

class DeckRepositoryImpl() : DeckRepository {

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