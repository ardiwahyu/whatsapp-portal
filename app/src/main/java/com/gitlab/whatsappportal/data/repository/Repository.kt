package com.gitlab.whatsappportal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.gitlab.whatsappportal.data.Resource
import com.gitlab.whatsappportal.data.Status
import com.gitlab.whatsappportal.data.db.dao.CountryDao
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.data.network.RequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(
    private val requestService: RequestService,
    private val countryDao: CountryDao
){
    private suspend fun saveCallCountry(){
        val countryResponse = requestService.listCountry()
        if (countryResponse.isSuccessful){
            val country = countryResponse.body()
            val listCountry = country?.map {
                CountryVo(
                    it.flag,
                    it.name,
                    it.callingCodes[0]
                )
            }
            listCountry?.let { countryDao.replace(it) }
        }else{
            val message = countryResponse.errorBody().toString()
            throw Exception(message)
        }
    }

    fun listCountry(refresh: Boolean): LiveData<Resource<List<CountryVo>>>{
        return liveData(Dispatchers.IO){
            val cache = countryDao.getAll()
            emit(Resource.loading(cache))
            if (refresh){
                try {
                    saveCallCountry()
                    emit(Resource.success(countryDao.getAll()))
                }catch (e: Exception){
                    print(e.message)
                }
            }
        }
    }

    suspend fun searchCountry(string: String): Resource<List<CountryVo>>{
        return withContext(Dispatchers.IO){
            val cache = countryDao.getByName(string)
            return@withContext Resource(Status.SUCCESS, cache, "")
        }
    }
}