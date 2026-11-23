package com.acs.oboeruapp.features.auth.ui

import com.acs.oboeruapp.features.auth.domain.model.AuthState

data class AuthUiState(
    val authState: AuthState = AuthState.Loading,
    val isSigningIn: Boolean = false,
    val errorMessage: String? = null,
) {
    val isAuthenticated: Boolean
        get() = authState is AuthState.Authenticated
}
