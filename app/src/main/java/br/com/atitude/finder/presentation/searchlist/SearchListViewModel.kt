package br.com.atitude.finder.presentation.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class SearchListViewModel(
    private val repository: ApiRepository,
    remoteConfig: AppRemoteConfig
) : BaseViewModel(remoteConfig) {

    private val _flow = MutableLiveData<Flow>(Flow.SearchingPoints)
    val flow: LiveData<Flow> = _flow

    private val _expandedSearchParams = MutableLiveData<Boolean>(false)
    val expandedSearchParams: LiveData<Boolean> = _expandedSearchParams

    fun toggleExpandSearchParams() {
        _expandedSearchParams.value = !(expandedSearchParams.value ?: false)
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
    }
}