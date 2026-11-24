package com.acs.oboeruapp.features.kana.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.acs.oboeruapp.core.data.local.KanaDatabaseSeeder
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.core.domain.srs.SrsCalculator
import com.acs.oboeruapp.core.domain.srs.SrsRating
import com.acs.oboeruapp.core.domain.srs.SrsState
import com.acs.oboeruapp.core.domain.time.TimeProvider
import com.acs.oboeruapp.db.OboeruDatabase
import com.acs.oboeruapp.db.SelectDueKanaWithProgress
import com.acs.oboeruapp.db.User_item_progress
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.domain.model.StudyItemType
import com.acs.oboeruapp.features.kana.domain.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProgressRepositoryImpl(
    private val database: OboeruDatabase,
    private val seeder: KanaDatabaseSeeder,
    private val timeProvider: TimeProvider,
) : ProgressRepository {

    override fun observeDueReviewCount(): Flow<Int> =
        database.userItemProgressQueries
            .selectScheduledReviewTimestamps()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { timestamps ->
                val now = timeProvider.nowEpochMillis()
                timestamps.count { it <= now }
            }
            .distinctUntilChanged()

    override suspend fun getDueKanaItems(): List<KanaItemWithProgress> = withContext(Dispatchers.IO) {
        seeder.ensureSeeded()
        val now = timeProvider.nowEpochMillis()
        database.kanaItemQueries
            .selectDueKanaWithProgress(nowEpochMillis = now)
            .executeAsList()
            .map { it.toDomain() }
    }

    override suspend fun recordReview(
        itemType: StudyItemType,
        itemId: String,
        rating: SrsRating,
    ) = withContext(Dispatchers.IO) {
        val now = timeProvider.nowEpochMillis()
        database.transaction {
            val current = database.userItemProgressQueries
                .selectByKey(itemType = itemType.name, itemId = itemId)
                .executeAsOneOrNull()
                ?.toSrsState()

            val next = SrsCalculator.calculateNext(
                current = current,
                rating = rating,
                nowEpochMillis = now,
            )

            database.userItemProgressQueries.upsert(
                itemType = itemType.name,
                itemId = itemId,
                status = next.status.name,
                srsStage = next.srsStage.toLong(),
                nextReviewAt = next.nextReviewAt,
                lastReviewedAt = now,
                correctCount = next.correctCount.toLong(),
                wrongCount = next.wrongCount.toLong(),
                updatedAt = now,
                isSynced = 0L,
            )
        }
    }

    override suspend fun setManualMastery(
        itemType: StudyItemType,
        itemId: String,
        mastered: Boolean,
    ) = withContext(Dispatchers.IO) {
        val now = timeProvider.nowEpochMillis()
        database.transaction {
            val existing = database.userItemProgressQueries
                .selectByKey(itemType = itemType.name, itemId = itemId)
                .executeAsOneOrNull()

            val next = SrsCalculator.manualMasteryToggle(
                current = existing?.toSrsState(),
                mastered = mastered,
                nowEpochMillis = now,
            )

            database.userItemProgressQueries.upsert(
                itemType = itemType.name,
                itemId = itemId,
                status = next.status.name,
                srsStage = next.srsStage.toLong(),
                nextReviewAt = next.nextReviewAt,
                lastReviewedAt = existing?.last_reviewed_at ?: now,
                correctCount = next.correctCount.toLong(),
                wrongCount = next.wrongCount.toLong(),
                updatedAt = now,
                isSynced = 0L,
            )
        }
    }
}

private fun User_item_progress.toSrsState(): SrsState = SrsState(
    status = ProgressStatus.fromString(status),
    srsStage = srs_stage.toInt(),
    nextReviewAt = next_review_at,
    correctCount = correct_count.toInt(),
    wrongCount = wrong_count.toInt(),
)

private fun SelectDueKanaWithProgress.toDomain(): KanaItemWithProgress = KanaItemWithProgress(
    item = KanaItem(
        id = id,
        script = KanaScript.fromString(script_type),
        category = KanaCategory.fromString(category),
        groupKey = group_key,
        character = character,
        romaji = romaji,
        exampleWord = example_word,
        exampleMeaning = example_meaning,
        rowIndex = row_index.toInt(),
        colIndex = col_index.toInt(),
        sortOrder = sort_order.toInt(),
    ),
    status = ProgressStatus.fromString(progress_status),
    srsStage = srs_stage.toInt(),
    nextReviewAt = next_review_at,
    lastReviewedAt = last_reviewed_at,
    correctCount = correct_count.toInt(),
    wrongCount = wrong_count.toInt(),
)
