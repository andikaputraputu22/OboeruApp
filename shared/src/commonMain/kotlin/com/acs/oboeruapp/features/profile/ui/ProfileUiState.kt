package com.acs.oboeruapp.features.profile.ui

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val photoUrl: String? = null,
    val isSigningOut: Boolean = false,
    val errorMessage: String? = null,
)
