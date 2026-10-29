package com.acs.oboeruapp

import com.acs.oboeruapp.features.learningspace.data.repository.LearningSpaceRepositoryImpl
import kotlin.test.Test
import kotlin.test.assertNotNull

class SharedCommonTest {

    @Test
    fun testLearningSpaceRepositoryInitialization() {
        val repo = LearningSpaceRepositoryImpl()
        assertNotNull(repo)
    }
}
