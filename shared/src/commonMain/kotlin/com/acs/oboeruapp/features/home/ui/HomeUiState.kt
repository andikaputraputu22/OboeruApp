package com.acs.oboeruapp.features.home.ui

import com.acs.oboeruapp.features.deck.domain.model.DeckBlock
import com.acs.oboeruapp.features.deck.domain.model.DeckHeader
import com.acs.oboeruapp.features.learningspace.domain.model.LearningSpace

data class HomeUiState(
    val isAuthenticated: Boolean = false,
    val photoUrl: String? = null,
    val displayName: String? = null,
    val learningSpaces: List<LearningSpace> = emptyList(),
    val selectedSpaceId: String = "",
    val deckHeader: DeckHeader? = null,
    val deckBlocks: List<DeckBlock> = emptyList()
)

