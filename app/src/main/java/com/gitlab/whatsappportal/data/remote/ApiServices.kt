package com.gitlab.whatsappportal.data.remote

import com.gitlab.whatsappportal.data.remote.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {
    @GET("all")
    suspend fun listCountry(): Response<List<Country>>
}