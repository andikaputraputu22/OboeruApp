package com.acs.oboeruapp.core.di

import com.acs.oboeruapp.AppViewModel
import com.acs.oboeruapp.core.data.remote.provideSupabaseClient
import com.acs.oboeruapp.features.auth.AuthViewModel
import com.acs.oboeruapp.features.auth.data.repository.AuthRepositoryImpl
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.deck.data.repository.DeckRepositoryImpl
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.home.HomeViewModel
import com.acs.oboeruapp.features.learningspace.data.repository.LearningSpaceRepositoryImpl
import com.acs.oboeruapp.features.learningspace.domain.repository.LearningSpaceRepository
import com.acs.oboeruapp.features.onboarding.data.repository.OnboardingRepositoryImpl
import com.acs.oboeruapp.features.onboarding.domain.repository.OnboardingRepository
import com.acs.oboeruapp.features.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(supabase = get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(dataStore = get()) }
    single<LearningSpaceRepository> { LearningSpaceRepositoryImpl() }
    single<DeckRepository> { DeckRepositoryImpl() }
}

val viewModelModule = module {
    viewModel {
        AppViewModel(
            authRepository = get(),
            onboardingRepository = get()
        )
    }
    viewModel {
        AuthViewModel(
            authRepository = get()
        )
    }
    viewModel {
        HomeViewModel(
            authRepository = get(),
            learningSpaceRepository = get(),
            deckRepository = get()
        )
    }
    viewModel {
        ProfileViewModel(
            authRepository = get()
        )
    }
}

val networkModule = module {
    single { provideSupabaseClient() }
}

val appModules = listOf(
    platformModule,
    repositoryModule,
    viewModelModule,
    networkModule
)

