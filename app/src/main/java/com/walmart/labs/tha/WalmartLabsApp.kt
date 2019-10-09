package com.walmart.labs.tha

import android.app.Application
import com.facebook.stetho.Stetho

class WalmartLabsApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        DI.getInstance(this)
    }
}