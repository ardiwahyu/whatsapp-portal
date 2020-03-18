package com.gitlab.whatsappportal.ui.country_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitlab.whatsappportal.data.Resource
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountryViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    val countryList = MutableLiveData<Resource<List<CountryVo>>>()

    fun getCountry(refresh: Boolean): LiveData<Resource<List<CountryVo>>> {
        return repository.listCountry(refresh)
    }

    fun searchCountry(string: String){
        viewModelScope.launch {
            countryList.postValue(repository.searchCountry("%$string%"))
        }
    }
}