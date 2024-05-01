package br.com.atitude.finder.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class SearchViewModel(
    private val apiRepository: ApiRepository,
    private val analyticsTracking: AnalyticsTracking,
    remoteConfig: AppRemoteConfig
) : BaseViewModel(remoteConfig) {

    private val _searchParams = MutableLiveData<SearchParams?>()
    val searchParams: LiveData<SearchParams?> = _searchParams

    fun trackCreateButton() {
        analyticsTracking.log("create_point")
    }

    fun trackSearchButton(
        postalCode: String?,
        weekDays: List<WeekDay>,
        categories: List<String>,
        times: List<String>
    ) {
        analyticsTracking.log("search_points") {
            postalCode?.takeIf { it.isNotBlank() }?.let {
                param("postal_code", it)
            }
            param("week_days", weekDays.joinToString(","))
            param("categories", categories.joinToString(","))
            param("times", times.joinToString(","))
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