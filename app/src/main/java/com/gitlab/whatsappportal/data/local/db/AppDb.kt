package com.gitlab.whatsappportal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gitlab.whatsappportal.data.local.db.dao.CountryDao
import com.gitlab.whatsappportal.data.local.db.vo.CountryVo

@Database(
    entities = [CountryVo::class],
    version = 1,
    exportSchema = false
)

abstract class AppDb: RoomDatabase() {
    abstract fun countryDao(): CountryDao
}