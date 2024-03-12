package br.com.atitude.finder.presentation.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class SearchListViewModel(
    private val repository: ApiRepository,
    private val remoteConfig: AppRemoteConfig,
    sharedPreferences: SharedPrefs
) : BaseViewModel(remoteConfig, sharedPreferences) {

    private val _flow = MutableLiveData<Flow>(Flow.SearchingPoints)
    val flow: LiveData<Flow> = _flow

    private val _expandedSearchParams = MutableLiveData(false)
    val expandedSearchParams: LiveData<Boolean> = _expandedSearchParams

    fun isSearchParamsViewEnabled() = remoteConfig.getBoolean("SearchParamsViewEnabled")

    fun toggleExpandSearchParams() {
        _expandedSearchParams.value = !(expandedSearchParams.value ?: false)
    }

    fun fetchAllPoints() {
        launch {
            val points = repository.getAllPoints()

            if (points.isEmpty()) _flow.postValue(Flow.NoPoints)
            else _flow.postValue(Flow.Success(points))
        }
    }

    fun deletePoint(id: String) {
        launch(loadingReason = "Deletando célula") {
            repository.deletePoint(id)
            _flow.postValue(Flow.DeletedPoint)
        }
    }

    fun search(
        postalCode: String,
        weekDays: List<String> = emptyList(),
        tags: List<String> = emptyList(),
        times: List<String>
    ) {
        launch {
            val points =
                repository.searchPoints(
                    postalCode = postalCode,
                    weekDays = weekDays,
                    tags = tags,
                    times = times
                )

            if (points.isEmpty()) _flow.postValue(Flow.NoPoints)
            else _flow.postValue(Flow.Success(points))
        }
    }

    sealed class Flow {
        data object SearchingPoints : Flow()
        data class Success(val points: List<SimplePoint>) : Flow()
        data object NoPoints : Flow()
        data object DeletedPoint: Flow()
    }
}