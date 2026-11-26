package com.acs.oboeruapp.features.kana.chart

import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KanaChartGridBuilderTest {

    @Test
    fun basicRowsPlaceCharactersAtExactFiveColumnSlots() {
        val items = listOf(
            sampleItem("hiragana_ya", KanaCategory.BASIC, "ya", "や", "ya", row = 7, col = 0),
            sampleItem("hiragana_yu", KanaCategory.BASIC, "ya", "ゆ", "yu", row = 7, col = 2),
            sampleItem("hiragana_yo", KanaCategory.BASIC, "ya", "よ", "yo", row = 7, col = 4),
            sampleItem("hiragana_wa", KanaCategory.BASIC, "wa", "わ", "wa", row = 9, col = 0),
            sampleItem("hiragana_wo", KanaCategory.BASIC, "wa", "を", "wo", row = 9, col = 4),
        )

        val rows = KanaChartGridBuilder.buildRows(items, KanaCategory.BASIC)

        assertEquals(2, rows.size)
        assertEquals(5, rows[0].cells.size)
        assertEquals("や", rows[0].cells[0]?.item?.character)
        assertNull(rows[0].cells[1])
        assertEquals("ゆ", rows[0].cells[2]?.item?.character)
        assertNull(rows[0].cells[3])
        assertEquals("よ", rows[0].cells[4]?.item?.character)

        assertEquals("わ", rows[1].cells[0]?.item?.character)
        assertNull(rows[1].cells[1])
        assertNull(rows[1].cells[2])
        assertNull(rows[1].cells[3])
        assertEquals("を", rows[1].cells[4]?.item?.character)
    }

    @Test
    fun yoonRowsUseThreeColumnsAndFilterOtherCategories() {
        val items = listOf(
            sampleItem("hiragana_a", KanaCategory.BASIC, "a", "あ", "a", row = 0, col = 0),
            sampleItem("hiragana_kya", KanaCategory.YOON, "kya", "きゃ", "kya", row = 0, col = 0),
            sampleItem("hiragana_kyu", KanaCategory.YOON, "kya", "きゅ", "kyu", row = 0, col = 1),
            sampleItem("hiragana_kyo", KanaCategory.YOON, "kya", "きょ", "kyo", row = 0, col = 2),
        )

        val rows = KanaChartGridBuilder.buildRows(items, KanaCategory.YOON)

        assertEquals(1, rows.size)
        assertEquals(3, rows[0].cells.size)
        assertNotNull(rows[0].cells[0])
        assertEquals("きゃ", rows[0].cells[0]?.item?.character)
        assertEquals("きゅ", rows[0].cells[1]?.item?.character)
        assertEquals("きょ", rows[0].cells[2]?.item?.character)
    }

    @Test
    fun emptyInputAndOutOfBoundsColumnsAreHandledSafely() {
        assertEquals(emptyList(), KanaChartGridBuilder.buildRows(emptyList(), KanaCategory.BASIC))
        assertEquals(listOf("A", "I", "U", "E", "O"), KanaChartGridBuilder.columnHeadersFor(KanaCategory.DAKUTEN))
        assertEquals(listOf("YA", "YU", "YO"), KanaChartGridBuilder.columnHeadersFor(KanaCategory.YOON))

        val items = listOf(
            sampleItem("hiragana_ga", KanaCategory.DAKUTEN, "ga", "が", "ga", row = 0, col = 0),
            sampleItem("invalid_col", KanaCategory.DAKUTEN, "ga", "?", "?", row = 0, col = 99),
        )
        val rows = KanaChartGridBuilder.buildRows(items, KanaCategory.DAKUTEN)

        assertEquals(1, rows.size)
        assertEquals(5, rows[0].cells.size)
        assertEquals("が", rows[0].cells[0]?.item?.character)
        assertNull(rows[0].cells[1])
    }

    private fun sampleItem(
        id: String,
        category: KanaCategory,
        groupKey: String,
        character: String,
        romaji: String,
        row: Int,
        col: Int,
    ) = KanaItemWithProgress(
        item = KanaItem(
            id = id,
            script = KanaScript.HIRAGANA,
            category = category,
            groupKey = groupKey,
            character = character,
            romaji = romaji,
            exampleWord = null,
            exampleMeaning = null,
            rowIndex = row,
            colIndex = col,
            sortOrder = row * 5 + col,
        )
    )
}
