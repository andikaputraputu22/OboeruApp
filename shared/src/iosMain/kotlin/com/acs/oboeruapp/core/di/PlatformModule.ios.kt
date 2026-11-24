package com.acs.oboeruapp.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.acs.oboeruapp.core.data.local.DATA_STORE_FILE_NAME
import com.acs.oboeruapp.core.data.local.DatabaseDriverFactory
import com.acs.oboeruapp.core.data.local.IosDatabaseDriverFactory
import com.acs.oboeruapp.core.data.local.createAppDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        createAppDataStore {
            val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = true,
                error = null,
            )
            requireNotNull(documentDirectory?.path) + "/$DATA_STORE_FILE_NAME"
        }
    }

    single<DatabaseDriverFactory> {
        IosDatabaseDriverFactory()
    }
}
