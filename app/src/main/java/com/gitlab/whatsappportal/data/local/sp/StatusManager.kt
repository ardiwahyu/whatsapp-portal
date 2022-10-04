package com.gitlab.whatsappportal.data.local.sp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.DisplayMetrics
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


class StatusManager @Inject constructor(@ApplicationContext context: Context) {
    companion object{
        internal const val SP_NAME = "whatsapp_portal"
        const val STATUS_KEY = "status"
        const val LANGUAGE_KEY = "language"
        const val COUNTRY_KEY = "country"
        const val CODE_KEY = "code"
    }

    private var sp: SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun storeStatus(boolean: Boolean){
        sp.edit().putBoolean(STATUS_KEY, boolean).apply()
    }

    fun getStatus(): Boolean{
        return sp.getBoolean(STATUS_KEY, false)
    }

    fun storeLanguage(string: String){
        sp.edit().putString(LANGUAGE_KEY, string).apply()
    }

    fun getLanguage(): String{
        return sp.getString(LANGUAGE_KEY, "in").toString()
    }

    fun storeCountry(string: String){
        sp.edit().putString(COUNTRY_KEY, string).apply()
    }

    fun getCountry(): String{
        return sp.getString(COUNTRY_KEY, "indonesia").toString()
    }

    fun storeCode(string: String){
        sp.edit().putString(CODE_KEY, string).apply()
    }

    fun getCode(): String{
        return sp.getString(CODE_KEY, "+62").toString()
    }
}