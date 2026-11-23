package com.acs.oboeruapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.acs.oboeruapp.core.data.local.DATA_STORE_FILE_NAME
import com.acs.oboeruapp.core.data.local.createAppDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        val context: Context = get()
        createAppDataStore {
            context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }
    }
}
