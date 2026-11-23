package com.acs.oboeruapp.core.data.remote

import com.acs.oboeruapp.core.config.AppConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

fun provideSupabaseClient(): SupabaseClient {
    require(AppConfig.SUPABASE_URL.isNotBlank() && AppConfig.SUPABASE_KEY.isNotBlank()) {
        "Missing SUPABASE_URL or SUPABASE_KEY in local.properties"
    }

    return createSupabaseClient(
        supabaseUrl = AppConfig.SUPABASE_URL,
        supabaseKey = AppConfig.SUPABASE_KEY
    ) {
        install(Auth) {
            host = "login-callback"
            scheme = "oboeruapp"
        }
        install(Postgrest)
    }
}