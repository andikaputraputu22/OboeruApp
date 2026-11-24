package com.acs.oboeruapp.features.kana.domain.repository

import com.acs.oboeruapp.core.domain.srs.SrsRating
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.StudyItemType
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun observeDueReviewCount(): Flow<Int>
    suspend fun getDueKanaItems(): List<KanaItemWithProgress>
    suspend fun recordReview(itemType: StudyItemType, itemId: String, rating: SrsRating)
    suspend fun setManualMastery(itemType: StudyItemType, itemId: String, mastered: Boolean)
}
