package com.acs.oboeruapp.features.kana.chart

import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress

data class KanaChartRow(
    val rowIndex: Int,
    val groupKey: String,
    val cells: List<KanaItemWithProgress?>,
)

object KanaChartGridBuilder {

    fun columnHeadersFor(category: KanaCategory): List<String> =
        when (category) {
            KanaCategory.BASIC,
            KanaCategory.DAKUTEN -> listOf("A", "I", "U", "E", "O")

            KanaCategory.YOON -> listOf("YA", "YU", "YO")
        }

    fun buildRows(
        items: List<KanaItemWithProgress>,
        category: KanaCategory,
    ): List<KanaChartRow> {
        val columnCount = columnHeadersFor(category).size
        val filtered = items.filter { it.item.category == category }
        if (filtered.isEmpty()) return emptyList()

        return filtered
            .groupBy { it.item.rowIndex }
            .toList()
            .sortedBy { (rowIndex, _) -> rowIndex }
            .map { (rowIndex, rowItems) ->
                val cells = MutableList<KanaItemWithProgress?>(columnCount) { null }
                rowItems.forEach { entry ->
                    val col = entry.item.colIndex
                    if (col in 0 until columnCount) {
                        cells[col] = entry
                    }
                }
                KanaChartRow(
                    rowIndex = rowIndex,
                    groupKey = rowItems.first().item.groupKey,
                    cells = cells.toList(),
                )
            }
    }
}
