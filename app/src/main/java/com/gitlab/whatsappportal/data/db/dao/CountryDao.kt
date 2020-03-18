package com.gitlab.whatsappportal.data.db.dao

import androidx.room.*
import com.gitlab.whatsappportal.data.db.vo.CountryVo

@Dao
abstract class CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(listCountryVo: List<CountryVo>)

    @Query("SELECT * FROM country")
    abstract suspend fun getAll(): List<CountryVo>

    @Query("DELETE FROM country")
    abstract suspend fun delete()

    @Query("SELECT * FROM country WHERE name LIKE :name")
    abstract suspend fun getByName(name: String): List<CountryVo>

    @Transaction
    open suspend fun replace(listCountryVo: List<CountryVo>){
        delete()
        insert(listCountryVo)
    }
}