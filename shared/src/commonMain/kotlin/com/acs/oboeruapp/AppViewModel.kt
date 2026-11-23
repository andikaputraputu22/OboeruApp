package com.acs.oboeruapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.features.auth.domain.model.AuthState
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.onboarding.domain.repository.OnboardingRepository
import com.acs.oboeruapp.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

data class AppUiState(
    val authState: AuthState = AuthState.Loading,
    val initialDestination: Screen? = null,
)

class AppViewModel(
    authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    private val initialDestination = MutableStateFlow<Screen?>(null)

    val uiState: StateFlow<AppUiState> = combine(
        authRepository.authState,
        initialDestination,
    ) { authState, startDestination ->
        AppUiState(
            authState = authState,
            initialDestination = startDestination,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AppUiState(),
    )

    init {
        viewModelScope.launch {
            val resolvedDestination = combine(
                authRepository.authState,
                onboardingRepository.hasCompletedOnboarding,
            ) { authState, hasCompletedOnboarding ->
                when (authState) {
                    is AuthState.Loading -> null
                    is AuthState.Authenticated -> Screen.Home
                    is AuthState.Unauthenticated -> {
                        if (hasCompletedOnboarding) Screen.Landing else Screen.Onboarding
                    }
                }
            }.first { it != null }

            initialDestination.value = resolvedDestination
        }

        viewModelScope.launch {
            combine(
                authRepository.authState,
                onboardingRepository.hasCompletedOnboarding,
            ) { authState, completed ->
                authState is AuthState.Authenticated && !completed
            }
                .filter { it }
                .take(1)
                .collect {
                    onboardingRepository.setOnboardingCompleted()
                }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingRepository.setOnboardingCompleted()
        }
    }
}
