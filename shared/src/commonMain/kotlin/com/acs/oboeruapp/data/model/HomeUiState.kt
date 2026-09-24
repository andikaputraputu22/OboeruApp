package com.acs.oboeruapp.data.model

data class HomeUiState(
    val isAuthenticated: Boolean = false,
    val photoUrl: String? = null,
    val displayName: String? = null,
    val learningSpaces: List<LearningSpace> = emptyList(),
    val selectedSpaceId: String = "",
    val deckHeader: DeckHeader? = null,
    val deckBlocks: List<DeckBlock> = emptyList()
)
