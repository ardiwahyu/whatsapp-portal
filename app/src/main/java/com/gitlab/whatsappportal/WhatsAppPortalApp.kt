package com.gitlab.whatsappportal

import com.facebook.stetho.Stetho
import com.gitlab.whatsappportal.data.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class WhatsAppPortalApp: DaggerApplication() {
    private val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun applicationInjector(): AndroidInjector<out WhatsAppPortalApp> {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        Stetho.initializeWithDefaults(this)
    }
}