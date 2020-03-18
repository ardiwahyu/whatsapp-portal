package com.gitlab.whatsappportal.data.di

import android.content.Context
import androidx.room.Room
import com.gitlab.whatsappportal.BuildConfig
import com.gitlab.whatsappportal.WhatsAppPortalApp
import com.gitlab.whatsappportal.data.db.AppDb
import com.gitlab.whatsappportal.data.db.dao.CountryDao
import com.gitlab.whatsappportal.data.network.RequestService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {
    private val connectionTimeout: Long = 50
    private val readTimeout: Long = 50
    private val writeTimeout: Long = 50

    @Provides
    fun providesContext(application: WhatsAppPortalApp): Context{
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideRequestService(): RequestService{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder().create()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://restcountries.eu/rest/v2/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(RequestService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Context): AppDb{
        return Room
            .databaseBuilder(app, AppDb::class.java, "whatsapp.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCountryDao(db: AppDb): CountryDao{
        return db.countryDao()
    }
}