package com.acs.oboeruapp

import android.app.Application
import com.acs.oboeruapp.injection.initKoin
import org.koin.android.ext.koin.androidContext

class OboeruApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@OboeruApp)
        }
    }
}