package com.gitlab.whatsappportal.ui.splash_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitlab.whatsappportal.data.Resource
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.data.network.model.Version
import com.gitlab.whatsappportal.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    val version = MutableLiveData<Resource<Version>>()
    fun getCountry(refresh: Boolean): LiveData<Resource<List<CountryVo>>>{
        return repository.listCountry(refresh)
    }

    fun getVersion(){
        viewModelScope.launch {
            version.postValue(repository.getVersion())
        }
    }
}