package com.acs.oboeruapp.core.data.local

import app.cash.sqldelight.db.SqlDriver

internal const val DATABASE_NAME = "oboeru.db"

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
