package com.gitlab.whatsappportal.data.sp

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import java.util.*
import javax.inject.Inject

class StatusManager @Inject constructor(context: Context) {
    companion object{
        internal const val SP_NAME = "whatsapp_portal"
        const val STATUS_KEY = "status"
        const val LANGUAGE_KEY = "language"
        const val COUNTRY_KEY = "country"
        const val CODE_KEY = "code"
        const val VERSION_KEY = "version"
    }

    private var sp: SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun storeVersion(string: String){
        sp.edit().putString(VERSION_KEY, string).apply()
    }

    fun getVersion(): String{
        return sp.getString(VERSION_KEY, "1").toString()
    }

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

    fun setLocale(context: Context, language: String){
        val locale = Locale(language)
        val res = context.resources
        val dm = res.displayMetrics
        val config = res.configuration
        config.setLocale(locale)
        res.updateConfiguration(config, dm)
    }
}