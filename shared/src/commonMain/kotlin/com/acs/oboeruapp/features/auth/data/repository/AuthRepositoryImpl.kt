package com.acs.oboeruapp.features.auth.data.repository

import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override val displayName: String?
        get() = "Oboeru"
}

