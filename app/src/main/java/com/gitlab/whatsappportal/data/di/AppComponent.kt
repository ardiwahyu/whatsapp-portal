package com.gitlab.whatsappportal.data.di

import com.gitlab.whatsappportal.WhatsAppPortalApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        AppModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent: AndroidInjector<WhatsAppPortalApp> {
    override fun inject(instance: WhatsAppPortalApp?)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: WhatsAppPortalApp): Builder

        fun build(): AppComponent
    }
}