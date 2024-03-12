package br.com.atitude.finder.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.analytics.tracking.SearchEvent
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class SearchViewModel(
    private val apiRepository: ApiRepository,
    private val analyticsTracking: AnalyticsTracking,
    remoteConfig: AppRemoteConfig,
    sharedPreferences: SharedPrefs
) : BaseViewModel(remoteConfig, sharedPreferences) {

    private val _searchParams = MutableLiveData<SearchParams?>()
    val searchParams: LiveData<SearchParams?> = _searchParams

    fun trackSearch(
        postalCode: String?,
        weekDays: List<WeekDay>,
        categories: List<String>,
        times: List<String>
    ) {
        SearchEvent(
            postalCode = postalCode,
            weekDays = weekDays,
            categories = categories,
            times = times
        ).run {
            analyticsTracking.log(this)
        }
    }

    fun fetchSearchParams() {
        launch(loadingReason = "Buscando informações das células...") {
            val searchParams: SearchParams = apiRepository.searchParams()

            if (searchParams.tags.isEmpty() || searchParams.times.isEmpty() || searchParams.weekDays.isEmpty()) {
                _searchParams.postValue(null)
            } else {
                _searchParams.postValue(searchParams)
            }

        }
    }

}