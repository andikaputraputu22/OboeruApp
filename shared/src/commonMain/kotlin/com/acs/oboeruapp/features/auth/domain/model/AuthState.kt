package com.acs.oboeruapp.features.auth.domain.model

sealed interface AuthState {
    data object Loading : AuthState
    data class Authenticated(val user: AuthUser) : AuthState
    data object Unauthenticated : AuthState
}
