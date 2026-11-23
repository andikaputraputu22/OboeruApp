package com.acs.oboeruapp.features.auth.domain.repository

import com.acs.oboeruapp.features.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>
    suspend fun signInWithGoogle(): Result<Unit>
    suspend fun signOut(): Result<Unit>
}

