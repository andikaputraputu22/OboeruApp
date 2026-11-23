package com.acs.oboeruapp.features.auth.data.repository

import com.acs.oboeruapp.features.auth.domain.model.AuthState
import com.acs.oboeruapp.features.auth.domain.model.AuthUser
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

class AuthRepositoryImpl(
    private val supabase: SupabaseClient
) : AuthRepository {

    override val authState: Flow<AuthState> = supabase.auth.sessionStatus
        .map { status ->
            when (status) {
                is SessionStatus.Initializing -> AuthState.Loading
                is SessionStatus.Authenticated -> {
                    val user = status.session.user ?: supabase.auth.currentUserOrNull()
                    if (user != null) {
                        AuthState.Authenticated(user.toAuthUser())
                    } else {
                        AuthState.Unauthenticated
                    }
                }
                is SessionStatus.RefreshFailure -> {
                    val cachedUser = supabase.auth.currentUserOrNull()
                    if (cachedUser != null) {
                        AuthState.Authenticated(cachedUser.toAuthUser())
                    } else {
                        AuthState.Unauthenticated
                    }
                }
                is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated
            }
        }
        .distinctUntilChanged()

    override suspend fun signInWithGoogle(): Result<Unit> = runAuthAction {
        supabase.auth.signInWith(Google)
    }

    override suspend fun signOut(): Result<Unit> = runAuthAction {
        supabase.auth.signOut()
    }

    private suspend fun runAuthAction(block: suspend () -> Unit): Result<Unit> {
        return try {
            block()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun UserInfo.toAuthUser(): AuthUser {
        val metadata = userMetadata
        return AuthUser(
            id = id,
            email = email,
            displayName = metadata?.stringOrNull("full_name")
                ?: metadata?.stringOrNull("name"),
            photoUrl = metadata?.stringOrNull("avatar_url")
                ?: metadata?.stringOrNull("picture"),
        )
    }

    private fun JsonObject.stringOrNull(key: String): String? =
        (this[key] as? JsonPrimitive)?.contentOrNull?.takeIf { it.isNotBlank() }
}

