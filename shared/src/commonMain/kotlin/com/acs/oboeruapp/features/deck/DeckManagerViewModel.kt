package com.acs.oboeruapp.features.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.StarterPack
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveBlockKey(
    val topic: DeckTopic,
    val activityType: DeckActivityType,
)

class DeckManagerViewModel(
    private val deckRepository: DeckRepository,
) : ViewModel() {

    val activeBlockKeys: StateFlow<Set<ActiveBlockKey>> = deckRepository
        .observeActiveBlocks()
        .map { blocks ->
            blocks.map { ActiveBlockKey(it.topic, it.activityType) }.toSet()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )

    private val isSubmitting = MutableStateFlow(false)

    fun addBlock(
        topic: DeckTopic,
        activityType: DeckActivityType,
        dailyTarget: Int,
        onComplete: () -> Unit = {},
    ) {
        if (!isSubmitting.compareAndSet(expect = false, update = true)) return
        viewModelScope.launch {
            try {
                deckRepository.addBlock(
                    topic = topic,
                    activityType = activityType,
                    dailyTarget = dailyTarget,
                )
                onComplete()
            } finally {
                isSubmitting.value = false
            }
        }
    }

    fun addStarterPack(
        pack: StarterPack,
        onComplete: () -> Unit = {},
    ) {
        if (!isSubmitting.compareAndSet(expect = false, update = true)) return
        viewModelScope.launch {
            try {
                deckRepository.addBlocks(pack.blocks)
                onComplete()
            } finally {
                isSubmitting.value = false
            }
        }
    }
}
