package com.acs.oboeruapp.features.kana.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.acs.oboeruapp.core.data.local.KanaDatabaseSeeder
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.db.Kana_item
import com.acs.oboeruapp.db.OboeruDatabase
import com.acs.oboeruapp.db.SelectByScriptWithProgress
import com.acs.oboeruapp.db.SelectMasterySummaryByScript
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaMasterySummary
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.domain.repository.KanaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class KanaRepositoryImpl(
    private val database: OboeruDatabase,
    private val seeder: KanaDatabaseSeeder,
) : KanaRepository {

    override suspend fun ensureSeeded() {
        seeder.ensureSeeded()
    }

    override fun observeKanaWithProgress(script: KanaScript): Flow<List<KanaItemWithProgress>> =
        database.kanaItemQueries
            .selectByScriptWithProgress(scriptType = script.name)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toDomain() } }
            .onStart { seeder.ensureSeeded() }

    override fun observeMasterySummary(): Flow<List<KanaMasterySummary>> =
        database.kanaItemQueries
            .selectMasterySummaryByScript()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.map { it.toDomain() } }
            .onStart { seeder.ensureSeeded() }

    override suspend fun getKanaById(id: String): KanaItem? = withContext(Dispatchers.IO) {
        seeder.ensureSeeded()
        database.kanaItemQueries
            .selectById(id = id)
            .executeAsOneOrNull()
            ?.toDomain()
    }
}

private fun Kana_item.toDomain(): KanaItem = KanaItem(
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
)

private fun SelectByScriptWithProgress.toDomain(): KanaItemWithProgress = KanaItemWithProgress(
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

private fun SelectMasterySummaryByScript.toDomain(): KanaMasterySummary = KanaMasterySummary(
    script = KanaScript.fromString(script_type),
    totalCount = total_count.toInt(),
    masteredCount = mastered_count.toInt(),
    learningCount = learning_count.toInt(),
)
