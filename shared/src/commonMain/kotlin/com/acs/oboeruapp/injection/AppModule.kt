package com.acs.oboeruapp.injection

import com.acs.oboeruapp.data.repository.AuthRepository
import com.acs.oboeruapp.data.repository.AuthRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
}

val appModules = listOf(
    repositoryModule
)