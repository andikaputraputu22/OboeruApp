package com.acs.oboeruapp.features.kana.domain.model

import com.acs.oboeruapp.core.domain.srs.ProgressStatus

enum class KanaScript {
    HIRAGANA,
    KATAKANA;

    companion object {
        fun fromString(value: String): KanaScript =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: HIRAGANA
    }
}

enum class KanaCategory {
    BASIC,
    DAKUTEN,
    YOON;

    companion object {
        fun fromString(value: String): KanaCategory =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: BASIC
    }
}

enum class StudyItemType {
    KANA;

    companion object {
        fun fromString(value: String): StudyItemType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: KANA
    }
}

data class KanaItem(
    val id: String,
    val script: KanaScript,
    val category: KanaCategory,
    val groupKey: String,
    val character: String,
    val romaji: String,
    val exampleWord: String?,
    val exampleMeaning: String?,
    val rowIndex: Int,
    val colIndex: Int,
    val sortOrder: Int,
)

data class KanaItemWithProgress(
    val item: KanaItem,
    val status: ProgressStatus = ProgressStatus.NEW,
    val srsStage: Int = 0,
    val nextReviewAt: Long? = null,
    val lastReviewedAt: Long? = null,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
)

data class KanaMasterySummary(
    val script: KanaScript,
    val totalCount: Int,
    val masteredCount: Int,
    val learningCount: Int,
) {
    val progressFraction: Float
        get() = if (totalCount == 0) 0f else (masteredCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)
}
