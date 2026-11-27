package com.acs.oboeruapp.core.di

import com.acs.oboeruapp.AppViewModel
import com.acs.oboeruapp.core.data.local.KanaDatabaseSeeder
import com.acs.oboeruapp.core.data.local.provideOboeruDatabase
import com.acs.oboeruapp.core.data.remote.provideSupabaseClient
import com.acs.oboeruapp.core.domain.time.SystemTimeProvider
import com.acs.oboeruapp.core.domain.time.TimeProvider
import com.acs.oboeruapp.features.auth.AuthViewModel
import com.acs.oboeruapp.features.auth.data.repository.AuthRepositoryImpl
import com.acs.oboeruapp.features.auth.domain.repository.AuthRepository
import com.acs.oboeruapp.features.deck.DeckManagerViewModel
import com.acs.oboeruapp.features.deck.data.repository.DeckRepositoryImpl
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.home.HomeViewModel
import com.acs.oboeruapp.features.kana.chart.KanaChartViewModel
import com.acs.oboeruapp.features.kana.data.repository.KanaRepositoryImpl
import com.acs.oboeruapp.features.kana.data.repository.ProgressRepositoryImpl
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.domain.repository.KanaRepository
import com.acs.oboeruapp.features.kana.domain.repository.ProgressRepository
import com.acs.oboeruapp.features.kana.flashcard.KanaFlashcardViewModel
import com.acs.oboeruapp.features.onboarding.data.repository.OnboardingRepositoryImpl
import com.acs.oboeruapp.features.onboarding.domain.repository.OnboardingRepository
import com.acs.oboeruapp.features.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(supabase = get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(dataStore = get()) }
    single<KanaRepository> { KanaRepositoryImpl(database = get(), seeder = get()) }
    single<ProgressRepository> {
        ProgressRepositoryImpl(
            database = get(),
            seeder = get(),
            timeProvider = get(),
        )
    }
    single<DeckRepository> {
        DeckRepositoryImpl(
            database = get(),
            progressRepository = get(),
            timeProvider = get(),
        )
    }
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
            deckRepository = get(),
        )
    }
    viewModel {
        DeckManagerViewModel(
            deckRepository = get(),
        )
    }
    viewModel { (initialScript: KanaScript) ->
        KanaChartViewModel(
            initialScript = initialScript,
            kanaRepository = get(),
            progressRepository = get(),
        )
    }
    viewModel { params ->
        KanaFlashcardViewModel(
            script = params.get(),
            blockId = params.getOrNull(),
            kanaRepository = get(),
            progressRepository = get(),
            deckRepository = get(),
            timeProvider = get(),
        )
    }
    viewModel {
        ProfileViewModel(
            authRepository = get()
        )
    }
}

val databaseModule = module {
    single<TimeProvider> { SystemTimeProvider() }
    single { provideOboeruDatabase(driverFactory = get()) }
    single { KanaDatabaseSeeder(database = get()) }
}

val networkModule = module {
    single { provideSupabaseClient() }
}

val appModules = listOf(
    platformModule,
    databaseModule,
    repositoryModule,
    viewModelModule,
    networkModule
)

