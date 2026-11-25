package com.acs.oboeruapp.features.deck.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.acs.oboeruapp.core.domain.streak.StreakCalculator
import com.acs.oboeruapp.core.domain.time.TimeProvider
import com.acs.oboeruapp.db.OboeruDatabase
import com.acs.oboeruapp.features.deck.domain.model.BlockOptionSpec
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.kana.domain.repository.ProgressRepository
import kotlin.concurrent.Volatile
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class DeckRepositoryImpl(
    private val database: OboeruDatabase,
    private val progressRepository: ProgressRepository,
    private val timeProvider: TimeProvider,
) : DeckRepository {

    private val seedMutex = Mutex()
    @Volatile
    private var starterChecked = false

    override suspend fun ensureStarterBlocks() {
        if (starterChecked) return
        seedMutex.withLock {
            if (starterChecked) return
            withContext(Dispatchers.IO) {
                val existingCount = database.userDeckBlockQueries.countAllBlocks().executeAsOne()
                if (existingCount > 0L) {
                    starterChecked = true
                    return@withContext
                }

                val now = timeProvider.nowEpochMillis()
                val starterBlocks = listOf(
                    StarterBlockSpec(
                        id = "starter_hiragana_chart",
                        topic = DeckTopic.HIRAGANA,
                        activityType = DeckActivityType.CHART,
                        dailyTarget = 0,
                        sortOrder = 0,
                    ),
                    StarterBlockSpec(
                        id = "starter_hiragana_flashcard",
                        topic = DeckTopic.HIRAGANA,
                        activityType = DeckActivityType.FLASHCARD,
                        dailyTarget = DeckActivityType.FLASHCARD.defaultDailyTarget,
                        sortOrder = 1,
                    ),
                    StarterBlockSpec(
                        id = "starter_hiragana_quiz",
                        topic = DeckTopic.HIRAGANA,
                        activityType = DeckActivityType.QUIZ,
                        dailyTarget = DeckActivityType.QUIZ.defaultDailyTarget,
                        sortOrder = 2,
                    ),
                    StarterBlockSpec(
                        id = "starter_todays_review",
                        topic = DeckTopic.ALL_REVIEW,
                        activityType = DeckActivityType.REVIEW,
                        dailyTarget = 0,
                        sortOrder = 3,
                    ),
                )

                database.transaction {
                    starterBlocks.forEach { spec ->
                        database.userDeckBlockQueries.upsert(
                            id = spec.id,
                            topic = spec.topic.name,
                            activityType = spec.activityType.name,
                            dailyTarget = spec.dailyTarget.toLong(),
                            sortOrder = spec.sortOrder.toLong(),
                            createdAt = now,
                            updatedAt = now,
                            deletedAt = null,
                            isSynced = 0L,
                        )
                    }
                }
                starterChecked = true
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeActiveBlocks(): Flow<List<UserDeckBlock>> {
        val blocksFlow = database.userDeckBlockQueries
            .selectActiveBlocks()
            .asFlow()
            .mapToList(Dispatchers.IO)

        val todayLogsFlow = flow {
            emit(timeProvider.todayIsoDate())
        }.flatMapLatest { today ->
            database.userDailyLogQueries
                .selectByDate(logDate = today)
                .asFlow()
                .mapToList(Dispatchers.IO)
        }

        return combine(
            blocksFlow,
            todayLogsFlow,
            progressRepository.observeDueReviewCount(),
        ) { blocks, todayLogs, dueCount ->
            val completedByBlockId = todayLogs.associate { it.block_id to it.completed_count.toInt() }

            blocks.map { row ->
                val topic = DeckTopic.fromString(row.topic)
                val activityType = DeckActivityType.fromString(row.activity_type)
                UserDeckBlock(
                    id = row.id,
                    topic = topic,
                    activityType = activityType,
                    dailyTarget = row.daily_target.toInt(),
                    sortOrder = row.sort_order.toInt(),
                    completedToday = completedByBlockId[row.id] ?: 0,
                    dueCount = if (activityType == DeckActivityType.REVIEW) dueCount else 0,
                )
            }
        }
            .distinctUntilChanged()
            .onStart { ensureStarterBlocks() }
    }

    override fun observeDailyStreak(): Flow<Int> =
        database.userDailyLogQueries
            .selectActiveDatesDesc()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { dates ->
                StreakCalculator.calculateStreak(
                    activeIsoDates = dates,
                    todayIsoDate = timeProvider.todayIsoDate(),
                )
            }
            .distinctUntilChanged()

    override suspend fun addBlock(
        topic: DeckTopic,
        activityType: DeckActivityType,
        dailyTarget: Int,
    ): Boolean = addBlocks(
        listOf(
            BlockOptionSpec(
                topic = topic,
                activityType = activityType,
                defaultTarget = dailyTarget,
            )
        )
    ) > 0

    override suspend fun addBlocks(blocks: List<BlockOptionSpec>): Int = withContext(Dispatchers.IO) {
        if (blocks.isEmpty()) return@withContext 0
        val now = timeProvider.nowEpochMillis()

        database.transactionWithResult {
            val activeBlocks = database.userDeckBlockQueries.selectActiveBlocks().executeAsList()
            var nextSortOrder = (activeBlocks.maxOfOrNull { it.sort_order } ?: -1L) + 1L
            var addedCount = 0

            blocks.forEach { spec ->
                val existing = database.userDeckBlockQueries
                    .selectActiveByTopicAndActivity(
                        topic = spec.topic.name,
                        activityType = spec.activityType.name,
                    )
                    .executeAsOneOrNull()

                if (existing == null) {
                    val sanitizedTarget = if (
                        spec.activityType == DeckActivityType.CHART ||
                        spec.activityType == DeckActivityType.REVIEW
                    ) {
                        0L
                    } else {
                        spec.defaultTarget.coerceAtLeast(1).toLong()
                    }

                    val id = "block_${spec.topic.name.lowercase()}_${spec.activityType.name.lowercase()}_${now}_${Random.nextInt(1000, 9999)}"
                    database.userDeckBlockQueries.upsert(
                        id = id,
                        topic = spec.topic.name,
                        activityType = spec.activityType.name,
                        dailyTarget = sanitizedTarget,
                        sortOrder = nextSortOrder,
                        createdAt = now,
                        updatedAt = now,
                        deletedAt = null,
                        isSynced = 0L,
                    )
                    nextSortOrder++
                    addedCount++
                }
            }

            addedCount
        }
    }

    override suspend fun updateDailyTarget(blockId: String, dailyTarget: Int) {
        withContext(Dispatchers.IO) {
            val now = timeProvider.nowEpochMillis()
            database.userDeckBlockQueries.updateDailyTarget(
                dailyTarget = dailyTarget.coerceAtLeast(1).toLong(),
                updatedAt = now,
                id = blockId,
            )
        }
    }

    override suspend fun removeBlock(blockId: String) {
        withContext(Dispatchers.IO) {
            val now = timeProvider.nowEpochMillis()
            database.userDeckBlockQueries.softDelete(
                deletedAt = now,
                updatedAt = now,
                id = blockId,
            )
        }
    }

    override suspend fun incrementDailyLog(blockId: String, delta: Int) {
        if (delta <= 0) return
        withContext(Dispatchers.IO) {
            val now = timeProvider.nowEpochMillis()
            val today = timeProvider.todayIsoDate()

            database.transaction {
                val existing = database.userDailyLogQueries
                    .selectByBlockAndDate(blockId = blockId, logDate = today)
                    .executeAsOneOrNull()
                val newCount = (existing?.completed_count ?: 0L) + delta.toLong()

                database.userDailyLogQueries.upsert(
                    blockId = blockId,
                    logDate = today,
                    completedCount = newCount,
                    updatedAt = now,
                    isSynced = 0L,
                )
            }
        }
    }

    private data class StarterBlockSpec(
        val id: String,
        val topic: DeckTopic,
        val activityType: DeckActivityType,
        val dailyTarget: Int,
        val sortOrder: Int,
    )
}
