package com.acs.oboeruapp.data.repository

interface AuthRepository {
    val displayName: String?
}

class AuthRepositoryImpl() : AuthRepository {

    override val displayName: String?
        get() = "Oboeru"
}