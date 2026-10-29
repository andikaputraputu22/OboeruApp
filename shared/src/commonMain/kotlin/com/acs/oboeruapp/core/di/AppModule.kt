package com.acs.oboeruapp.core.di

import com.acs.oboeruapp.features.auth.data.repository.AuthRepositoryImpl
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.deck.data.repository.DeckRepositoryImpl
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.home.HomeViewModel
import com.acs.oboeruapp.features.learningspace.data.repository.LearningSpaceRepositoryImpl
import com.acs.oboeruapp.features.learningspace.domain.repository.LearningSpaceRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<LearningSpaceRepository> { LearningSpaceRepositoryImpl() }
    single<DeckRepository> { DeckRepositoryImpl() }
}

val viewModelModule = module {
    viewModel {
        HomeViewModel(
            authRepository = get(),
            learningSpaceRepository = get(),
            deckRepository = get()
        )
    }
}

val appModules = listOf(
    repositoryModule,
    viewModelModule
)

