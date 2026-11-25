package com.acs.oboeruapp.features.deck.domain.repository

import com.acs.oboeruapp.features.deck.domain.model.BlockOptionSpec
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    suspend fun ensureStarterBlocks()
    fun observeActiveBlocks(): Flow<List<UserDeckBlock>>
    fun observeDailyStreak(): Flow<Int>
    suspend fun addBlock(
        topic: DeckTopic,
        activityType: DeckActivityType,
        dailyTarget: Int = activityType.defaultDailyTarget,
    ): Boolean
    suspend fun addBlocks(blocks: List<BlockOptionSpec>): Int
    suspend fun updateDailyTarget(blockId: String, dailyTarget: Int)
    suspend fun removeBlock(blockId: String)
    suspend fun incrementDailyLog(blockId: String, delta: Int = 1)
}
