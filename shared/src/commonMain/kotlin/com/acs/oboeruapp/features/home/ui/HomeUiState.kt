package com.acs.oboeruapp.features.home.ui

import com.acs.oboeruapp.features.deck.domain.model.DailySummary
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock

data class HomeUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val photoUrl: String? = null,
    val displayName: String? = null,
    val deckBlocks: List<UserDeckBlock> = emptyList(),
    val dailySummary: DailySummary = DailySummary(),
)
