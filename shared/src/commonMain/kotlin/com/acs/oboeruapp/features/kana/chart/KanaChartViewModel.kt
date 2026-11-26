package com.acs.oboeruapp.features.kana.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.features.kana.chart.ui.KanaChartUiState
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.domain.model.StudyItemType
import com.acs.oboeruapp.features.kana.domain.repository.KanaRepository
import com.acs.oboeruapp.features.kana.domain.repository.ProgressRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KanaChartViewModel(
    initialScript: KanaScript,
    private val kanaRepository: KanaRepository,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val selectedScript = MutableStateFlow(initialScript)
    private val selectedCategory = MutableStateFlow(KanaCategory.BASIC)
    private val selectedItemId = MutableStateFlow<String?>(null)
    private val isUpdatingMastery = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scriptItemsFlow = selectedScript.flatMapLatest { script ->
        kanaRepository.observeKanaWithProgress(script).map { items -> script to items }
    }

    val uiState: StateFlow<KanaChartUiState> = combine(
        scriptItemsFlow,
        selectedCategory,
        selectedItemId,
    ) { (script, items), category, activeItemId ->
        val masteredCount = items.count { it.status == ProgressStatus.MASTERED }
        val learningCount = items.count { it.status == ProgressStatus.LEARNING }
        val rows = KanaChartGridBuilder.buildRows(items = items, category = category)
        val selectedItem = activeItemId?.let { id -> items.firstOrNull { it.item.id == id } }

        KanaChartUiState(
            isLoading = false,
            selectedScript = script,
            selectedCategory = category,
            columnHeaders = KanaChartGridBuilder.columnHeadersFor(category),
            rows = rows,
            masteredCount = masteredCount,
            learningCount = learningCount,
            totalCount = items.size,
            selectedItem = selectedItem,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = KanaChartUiState(selectedScript = initialScript),
    )

    fun onScriptSelected(script: KanaScript) {
        if (selectedScript.value != script) {
            selectedItemId.value = null
            selectedScript.value = script
        }
    }

    fun onCategorySelected(category: KanaCategory) {
        selectedCategory.value = category
    }

    fun onCharacterClick(item: KanaItemWithProgress) {
        selectedItemId.value = item.item.id
    }

    fun onDismissDetail() {
        selectedItemId.value = null
    }

    fun onToggleMastery(item: KanaItemWithProgress) {
        if (!isUpdatingMastery.compareAndSet(expect = false, update = true)) return
        val targetMastered = item.status != ProgressStatus.MASTERED
        viewModelScope.launch {
            try {
                progressRepository.setManualMastery(
                    itemType = StudyItemType.KANA,
                    itemId = item.item.id,
                    mastered = targetMastered,
                )
            } finally {
                isUpdatingMastery.value = false
            }
        }
    }
}
