package com.acs.oboeruapp.injection

import com.acs.oboeruapp.data.repository.AuthRepository
import com.acs.oboeruapp.data.repository.AuthRepositoryImpl
import com.acs.oboeruapp.data.repository.DeckRepository
import com.acs.oboeruapp.data.repository.DeckRepositoryImpl
import com.acs.oboeruapp.data.repository.LearningSpaceRepository
import com.acs.oboeruapp.data.repository.LearningSpaceRepositoryImpl
import com.acs.oboeruapp.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<LearningSpaceRepository> { LearningSpaceRepositoryImpl() }
    single<DeckRepository> { DeckRepositoryImpl() }
}

val viewModelModule = module {
    viewModel { HomeViewModel(
        authRepository = get(),
        learningSpaceRepository = get(),
        deckRepository = get()
    ) }
}

val appModules = listOf(
    repositoryModule,
    viewModelModule
)