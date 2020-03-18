package com.gitlab.whatsappportal.ui.splash_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gitlab.whatsappportal.data.Resource
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.data.repository.Repository
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun getCountry(refresh: Boolean): LiveData<Resource<List<CountryVo>>>{
        return repository.listCountry(refresh)
    }
}