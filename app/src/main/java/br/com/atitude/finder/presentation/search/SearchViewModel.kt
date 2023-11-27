package br.com.atitude.finder.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class SearchViewModel(
    private val apiRepository: ApiRepository
) : BaseViewModel() {

    private val _searchParams = MutableLiveData<SearchParams?>()
    val searchParams: LiveData<SearchParams?> = _searchParams

    fun fetchSearchParams() {
        launch {
            val searchParams: SearchParams = apiRepository.searchParams()

            if (searchParams.tags.isEmpty() || searchParams.times.isEmpty() || searchParams.weekDays.isEmpty()) {
                _searchParams.postValue(null)
            } else {
                _searchParams.postValue(searchParams)
            }

        }
    }

}