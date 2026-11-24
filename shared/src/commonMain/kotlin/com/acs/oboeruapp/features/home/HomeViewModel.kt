package com.acs.oboeruapp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.features.auth.domain.model.AuthState
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.deck.domain.model.DailySummary
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.home.ui.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    authRepository: AuthRepository,
    deckRepository: DeckRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        authRepository.authState,
        deckRepository.observeActiveBlocks(),
        deckRepository.observeDailyStreak(),
    ) { authState, blocks, streakDays ->
        val authenticatedUser = (authState as? AuthState.Authenticated)?.user
        val resolvedName = authenticatedUser?.displayName
            ?: authenticatedUser?.email?.substringBefore("@")

        val goalBlocks = blocks.filter { it.isGoalBased }
        val completedGoals = goalBlocks.count { it.isCompletedToday }

        HomeUiState(
            isLoading = false,
            isAuthenticated = authenticatedUser != null,
            photoUrl = authenticatedUser?.photoUrl,
            displayName = resolvedName,
            deckBlocks = blocks,
            dailySummary = DailySummary(
                completedGoals = completedGoals,
                totalGoals = goalBlocks.size,
                streakDays = streakDays,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )
}
