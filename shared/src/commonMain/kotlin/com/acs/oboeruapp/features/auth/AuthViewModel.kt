package com.acs.oboeruapp.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.auth.ui.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val isSigningIn = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AuthUiState> = combine(
        authRepository.authState,
        isSigningIn,
        errorMessage,
    ) { authState, signingIn, error ->
        AuthUiState(
            authState = authState,
            isSigningIn = signingIn,
            errorMessage = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AuthUiState(),
    )

    fun signInWithGoogle() {
        if (isSigningIn.value) return

        viewModelScope.launch {
            isSigningIn.value = true
            errorMessage.value = null

            try {
                authRepository.signInWithGoogle()
                    .onFailure { throwable ->
                        errorMessage.value = throwable.message ?: "Failed to sign in with Google."
                    }
            } finally {
                isSigningIn.value = false
            }
        }
    }

    fun clearError() {
        errorMessage.value = null
    }
}
