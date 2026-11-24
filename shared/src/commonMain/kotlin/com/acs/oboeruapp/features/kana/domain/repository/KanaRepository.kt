package com.acs.oboeruapp.features.kana.domain.repository

import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaMasterySummary
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import kotlinx.coroutines.flow.Flow

interface KanaRepository {
    suspend fun ensureSeeded()
    fun observeKanaWithProgress(script: KanaScript): Flow<List<KanaItemWithProgress>>
    fun observeMasterySummary(): Flow<List<KanaMasterySummary>>
    suspend fun getKanaById(id: String): KanaItem?
}
