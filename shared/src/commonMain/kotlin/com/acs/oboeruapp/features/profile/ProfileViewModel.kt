package com.acs.oboeruapp.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.features.auth.domain.model.AuthState
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.profile.ui.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val isSigningOut = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.authState,
        isSigningOut,
        errorMessage,
    ) { authState, signingOut, error ->
        val user = (authState as? AuthState.Authenticated)?.user
        val resolvedName = user?.displayName
            ?: user?.email?.substringBefore("@")
            ?: "Oboeru"

        ProfileUiState(
            userName = resolvedName,
            userEmail = user?.email.orEmpty(),
            photoUrl = user?.photoUrl,
            isSigningOut = signingOut,
            errorMessage = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState(),
    )

    fun signOut() {
        if (isSigningOut.value) return

        viewModelScope.launch {
            isSigningOut.value = true
            errorMessage.value = null

            try {
                authRepository.signOut()
                    .onFailure { throwable ->
                        errorMessage.value = throwable.message ?: "Failed to log out."
                    }
            } finally {
                isSigningOut.value = false
            }
        }
    }
}
