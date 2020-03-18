package com.gitlab.whatsappportal.data.di

import com.gitlab.whatsappportal.ui.country_activity.CountryActivity
import com.gitlab.whatsappportal.ui.main_activity.MainActivity
import com.gitlab.whatsappportal.ui.splash_activity.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeCountryActivity(): CountryActivity
}