package com.acs.oboeruapp.features.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    val hasCompletedOnboarding: Flow<Boolean>
    suspend fun setOnboardingCompleted()
}
