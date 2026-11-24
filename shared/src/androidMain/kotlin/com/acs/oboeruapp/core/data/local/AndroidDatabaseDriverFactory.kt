package com.acs.oboeruapp.core.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.acs.oboeruapp.db.OboeruDatabase

internal class AndroidDatabaseDriverFactory(
    private val context: Context,
) : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = OboeruDatabase.Schema,
        context = context,
        name = DATABASE_NAME,
    )
}
