package com.acs.oboeruapp.core.data.local

import com.acs.oboeruapp.db.OboeruDatabase

internal fun provideOboeruDatabase(
    driverFactory: DatabaseDriverFactory,
): OboeruDatabase = OboeruDatabase(driver = driverFactory.createDriver())
