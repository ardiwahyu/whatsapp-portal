package com.gitlab.whatsappportal.di

import android.content.Context
import androidx.room.Room
import com.gitlab.whatsappportal.data.local.db.AppDb
import com.gitlab.whatsappportal.data.local.db.dao.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext app: Context): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "whatsapp.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCountryDao(db: AppDb): CountryDao {
        return db.countryDao()
    }
}