package com.gitlab.whatsappportal.data.repository.country

import android.util.Log
import com.gitlab.whatsappportal.data.Resource
import com.gitlab.whatsappportal.data.local.db.dao.CountryDao
import com.gitlab.whatsappportal.data.local.db.vo.CountryVo
import com.gitlab.whatsappportal.data.remote.ApiServices
import com.gitlab.whatsappportal.data.remote.model.Country
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val countryDao: CountryDao
){
    suspend fun listCountry(): Flow<Resource<List<CountryVo>>> {
        return flow {
            emit(Resource.OnLoading())
            try {
                val listCountry = getFromLocal()
                if (listCountry.isEmpty()) {
                    getFromApi().let {
                        when(it) {
                            is Resource.OnError -> emit(Resource.OnError(it.message))
                            is Resource.OnSuccess -> emit(Resource.OnSuccess(it.data, it.message))
                            else -> {}
                        }
                    }
                } else {
                    emit(Resource.OnSuccess(listCountry, ""))
                }
            } catch (e: Exception) {
                emit(Resource.OnError("error: "+e.localizedMessage))
            }
        }
    }

    suspend fun searchCountry(search: String): Flow<Resource<List<CountryVo>>> {
        return flow {
            emit(Resource.OnLoading())
            try {
                emit(Resource.OnSuccess(countryDao.getByName("%$search%"), ""))
            } catch (e: Exception) {
                emit(Resource.OnError("error "+e.localizedMessage))
            }
        }
    }

    private suspend fun getFromLocal(): List<CountryVo> {
        return withContext(Dispatchers.IO) {
            return@withContext countryDao.getAll()
        }
    }

    private suspend fun getFromApi(): Resource<List<CountryVo>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiServices.listCountry()
                if (response.isSuccessful) {
                    countryDao.insert(response.body()!!.map {
                        CountryVo(
                            flag = it.flags.png,
                            name = it.name.common,
                            callingCode = Gson().toJson(it.callingCodes)
                        )
                    })
                    val listCountry = getFromLocal()
                    return@withContext Resource.OnSuccess(listCountry, "")
                } else {
                    return@withContext Resource.OnError(Gson().toJson(response.errorBody()))
                }
            } catch (e: Exception) {
                return@withContext Resource.OnError("error: "+e.localizedMessage)
            }
        }
    }
}