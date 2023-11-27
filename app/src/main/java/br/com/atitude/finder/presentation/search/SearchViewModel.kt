package br.com.atitude.finder.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.analytics.tracking.SearchEvent
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class SearchViewModel(
    private val apiRepository: ApiRepository,
    private val analyticsTracking: AnalyticsTracking
) : BaseViewModel() {

    private val _searchParams = MutableLiveData<SearchParams?>()
    val searchParams: LiveData<SearchParams?> = _searchParams

    fun trackSearch(postalCode: String) {
        SearchEvent(postalCode).run {
            analyticsTracking.log(this)
        }
    }

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