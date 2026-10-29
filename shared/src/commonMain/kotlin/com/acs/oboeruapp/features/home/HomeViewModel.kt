package com.acs.oboeruapp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.home.ui.HomeUiState
import com.acs.oboeruapp.features.learningspace.domain.repository.LearningSpaceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val learningSpaceRepository: LearningSpaceRepository,
    private val deckRepository: DeckRepository
) : ViewModel() {

    private val selectedSpaceId = MutableStateFlow<String?>(null)

    private val spacesState = learningSpaceRepository.getSpaces()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val deckState = selectedSpaceId
        .filterNotNull()
        .flatMapLatest { spaceId ->
            deckRepository.getDeckForSpace(spaceId)
        }

    val uiState: StateFlow<HomeUiState> = combine(
        spacesState,
        selectedSpaceId,
        deckState
    ) { spaces, selectedId, deck ->
        HomeUiState(
            isAuthenticated = false,
            photoUrl = null,
            displayName = null,
            learningSpaces = spaces,
            selectedSpaceId = selectedId ?: spaces.firstOrNull()?.id ?: "",
            deckHeader = deck.header,
            deckBlocks = deck.blocks
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        viewModelScope.launch {
            spacesState.firstOrNull()?.firstOrNull()?.let { firstSpace ->
                if (selectedSpaceId.value == null) {
                    selectedSpaceId.value = firstSpace.id
                }
            }
        }
    }

    fun onSpaceSelected(spaceId: String) {
        selectedSpaceId.value = spaceId
    }
}

