package com.acs.oboeruapp.injection

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSURL

object IosAuthBridge : KoinComponent {

    private val supabase: SupabaseClient by inject()

    fun handleDeepLink(url: NSURL) {
        supabase.handleDeeplinks(url)
    }
}