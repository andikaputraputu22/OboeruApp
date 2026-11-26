package com.acs.oboeruapp.features.kana.chart.ui

import com.acs.oboeruapp.features.kana.chart.KanaChartRow
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript

data class KanaChartUiState(
    val isLoading: Boolean = true,
    val selectedScript: KanaScript = KanaScript.HIRAGANA,
    val selectedCategory: KanaCategory = KanaCategory.BASIC,
    val columnHeaders: List<String> = listOf("A", "I", "U", "E", "O"),
    val rows: List<KanaChartRow> = emptyList(),
    val masteredCount: Int = 0,
    val learningCount: Int = 0,
    val totalCount: Int = 0,
    val selectedItem: KanaItemWithProgress? = null,
) {
    val masteryFraction: Float
        get() = if (totalCount == 0) 0f else (masteredCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)
}
