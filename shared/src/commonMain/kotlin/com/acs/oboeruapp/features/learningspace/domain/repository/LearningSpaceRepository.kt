package com.acs.oboeruapp.features.learningspace.domain.repository

import com.acs.oboeruapp.features.learningspace.domain.model.LearningSpace
import kotlinx.coroutines.flow.Flow

interface LearningSpaceRepository {
    fun getSpaces(): Flow<List<LearningSpace>>
}

