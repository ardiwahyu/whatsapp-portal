package com.gitlab.whatsappportal.data.network

import com.gitlab.whatsappportal.data.network.model.Country
import com.gitlab.whatsappportal.data.network.model.Version
import retrofit2.Response
import retrofit2.http.GET

interface RequestService {
    @GET("redirect.php")
    suspend fun listCountry(): Response<List<Country>>

    @GET("version.php")
    suspend fun getVersion(): Response<Version>
}