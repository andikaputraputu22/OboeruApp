package com.acs.oboeruapp.features.onboarding.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.acs.oboeruapp.features.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class OnboardingRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : OnboardingRepository {

    override val hasCompletedOnboarding: Flow<Boolean> = dataStore.data
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[KEY_HAS_COMPLETED_ONBOARDING] ?: false
        }
        .distinctUntilChanged()

    override suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[KEY_HAS_COMPLETED_ONBOARDING] = true
        }
    }

    private companion object {
        val KEY_HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    }
}
