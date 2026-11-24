package com.acs.oboeruapp.core.data.local

import com.acs.oboeruapp.db.OboeruDatabase
import kotlin.concurrent.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import oboeruapp.shared.generated.resources.Res

@Serializable
internal data class KanaSeedDto(
    val id: String,
    val scriptType: String,
    val category: String,
    val groupKey: String,
    val character: String,
    val romaji: String,
    val exampleWord: String? = null,
    val exampleMeaning: String? = null,
    val rowIndex: Long,
    val colIndex: Long,
    val sortOrder: Long,
)

class KanaDatabaseSeeder(
    private val database: OboeruDatabase,
) {
    private val seedMutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }
    @Volatile
    private var isSeeded = false

    suspend fun ensureSeeded() {
        if (isSeeded) return
        seedMutex.withLock {
            if (isSeeded) return
            withContext(Dispatchers.IO) {
                val existingCount = database.kanaItemQueries.countAll().executeAsOne()
                if (existingCount >= EXPECTED_KANA_COUNT) {
                    isSeeded = true
                    return@withContext
                }

                val rawJson = Res.readBytes(KANA_ASSET_PATH).decodeToString()
                val items = json.decodeFromString<List<KanaSeedDto>>(rawJson)

                database.transaction {
                    items.forEach { item ->
                        database.kanaItemQueries.insertOrReplace(
                            id = item.id,
                            scriptType = item.scriptType,
                            category = item.category,
                            groupKey = item.groupKey,
                            character = item.character,
                            romaji = item.romaji,
                            exampleWord = item.exampleWord,
                            exampleMeaning = item.exampleMeaning,
                            rowIndex = item.rowIndex,
                            colIndex = item.colIndex,
                            sortOrder = item.sortOrder,
                        )
                    }
                }
                isSeeded = true
            }
        }
    }

    private companion object {
        const val KANA_ASSET_PATH = "files/kana_data.json"
        const val EXPECTED_KANA_COUNT = 208L
    }
}
