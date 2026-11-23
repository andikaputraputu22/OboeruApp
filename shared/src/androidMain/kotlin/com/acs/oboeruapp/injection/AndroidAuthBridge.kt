package com.acs.oboeruapp.injection

import android.content.Intent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AndroidAuthBridge : KoinComponent {

    private val supabase: SupabaseClient by inject()

    fun handleDeepLink(intent: Intent?) {
        if (intent?.action != Intent.ACTION_VIEW || intent.data == null) return
        supabase.handleDeeplinks(intent)
    }
}
