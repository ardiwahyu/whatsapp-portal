package com.gitlab.whatsappportal.data.network

import com.gitlab.whatsappportal.data.network.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface RequestService {
    @GET("all?fields=name;callingCodes;flag")
    suspend fun listCountry(): Response<List<Country>>
}