package com.acs.oboeruapp.data.repository

import com.acs.oboeruapp.data.model.LearningSpace
import com.acs.oboeruapp.utils.dummyLearningSpaces
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface LearningSpaceRepository {
    fun getSpaces(): Flow<List<LearningSpace>>
}

class LearningSpaceRepositoryImpl() : LearningSpaceRepository {

    override fun getSpaces(): Flow<List<LearningSpace>> {
        return flowOf(dummyLearningSpaces)
    }
}