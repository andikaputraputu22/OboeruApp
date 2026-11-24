package com.acs.oboeruapp.core.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.acs.oboeruapp.db.OboeruDatabase

internal class IosDatabaseDriverFactory : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = OboeruDatabase.Schema,
        name = DATABASE_NAME,
    )
}
