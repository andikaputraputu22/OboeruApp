package com.acs.oboeruapp.features.auth.domain.model

data class AuthUser(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
)
