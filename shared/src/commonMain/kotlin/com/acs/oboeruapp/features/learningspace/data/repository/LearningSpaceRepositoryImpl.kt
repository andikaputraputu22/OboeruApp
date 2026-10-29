package com.acs.oboeruapp.features.learningspace.data.repository

import com.acs.oboeruapp.features.learningspace.domain.model.LearningSpace
import com.acs.oboeruapp.features.learningspace.domain.repository.LearningSpaceRepository
import com.acs.oboeruapp.utils.dummyLearningSpaces
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class LearningSpaceRepositoryImpl : LearningSpaceRepository {

    override fun getSpaces(): Flow<List<LearningSpace>> {
        return flowOf(dummyLearningSpaces)
    }
}

