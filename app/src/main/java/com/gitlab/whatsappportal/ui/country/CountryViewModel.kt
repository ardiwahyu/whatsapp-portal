package com.gitlab.whatsappportal.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitlab.whatsappportal.data.Resource
import com.gitlab.whatsappportal.data.local.db.vo.CountryVo
import com.gitlab.whatsappportal.data.repository.country.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    private val _successState = MutableLiveData<List<CountryVo>?>()
    val successState: LiveData<List<CountryVo>?> get() = _successState

    fun getCountry(){
        viewModelScope.launch {
            repository.listCountry().collect {
                when(it) {
                    is Resource.OnError -> _errorState.postValue(it.message)
                    is Resource.OnLoading -> _loadingState.postValue(true)
                    is Resource.OnSuccess -> _successState.postValue(it.data)
                }
                if (it !is Resource.OnLoading) {
                    _loadingState.postValue(false)
                }
            }
        }
    }

    fun searchCountry(search: String) {
        viewModelScope.launch {
            repository.searchCountry(search).collect {
                when(it) {
                    is Resource.OnError -> _errorState.postValue(it.message)
                    is Resource.OnLoading -> _loadingState.postValue(true)
                    is Resource.OnSuccess -> _successState.postValue(it.data)
                }
                if (it !is Resource.OnLoading) {
                    _loadingState.postValue(false)
                }
            }
        }
    }
}