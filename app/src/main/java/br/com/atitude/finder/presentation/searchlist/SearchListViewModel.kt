package br.com.atitude.finder.presentation.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.data.remoteconfig.Constants.CAN_DELETE_POINT
import br.com.atitude.finder.data.remoteconfig.Constants.SEARCH_PARAMS_VIEW_ENABLED
import br.com.atitude.finder.data.remoteconfig.Constants.SEARCH_V2
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class SearchListViewModel(
    private val repository: ApiRepository,
    private val remoteConfig: AppRemoteConfig,
    private val analyticsTracking: AnalyticsTracking
) : BaseViewModel(remoteConfig) {

    private val _flow = MutableLiveData<Flow>(Flow.SearchingPoints)
    val flow: LiveData<Flow> = _flow

    private val _expandedSearchParams = MutableLiveData(false)
    val expandedSearchParams: LiveData<Boolean> = _expandedSearchParams

    fun trackSeeDetails(simplePoint: SimplePoint) {
        analyticsTracking.log("see_point_details") {
            param("id", simplePoint.id)
            param("name", simplePoint.name)
            param("state", simplePoint.state.label)
        }
    }

    fun trackSaveState(simplePoint: SimplePoint) {
        analyticsTracking.log("save_point_state") {
            param("id", simplePoint.id)
            param("name", simplePoint.name)
            param("state", simplePoint.state.label)
        }
    }

    fun trackClickDeletePointButton() {
        analyticsTracking.log("click_delete_point_button")
    }

    fun trackSuccessDeletePoint(point: SimplePoint) {
        analyticsTracking.log("delete_point") {
            param("id", point.id)
            param("name", point.name)
        }
    }

    fun isSearchParamsViewEnabled() = remoteConfig.getBoolean(SEARCH_PARAMS_VIEW_ENABLED)

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

    fun searchByAddressOrPostalCode(
        input: String,
        weekDays: List<String> = emptyList(),
        tags: List<String> = emptyList(),
        times: List<String>
    ) {
        launch {
            val points =
                repository.searchPointsByAddressOrPostalCode(
                    input = input,
                    weekDays = weekDays,
                    tags = tags,
                    times = times
                )

            if (points.isEmpty()) _flow.postValue(Flow.NoPoints)
            else _flow.postValue(Flow.Success(points))
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

    private fun setPointState(loadingReason: String, point: SimplePoint, state: PointState) {
        launch(loadingReason = loadingReason) {
            repository.updatePoint(point.id, state)
            _flow.postValue(Flow.UpdatedPoint)
        }
    }

    fun setPointInactive(loadingReason: String, point: SimplePoint) {
        setPointState(loadingReason, point, PointState.INACTIVE)
    }

    fun setPointSuspended(loadingReason: String, point: SimplePoint) {
        setPointState(loadingReason, point, PointState.SUSPENDED)
    }

    fun setPointActive(loadingReason: String, point: SimplePoint) {
        setPointState(loadingReason, point, PointState.ACTIVE)
    }

    fun canDeletePoint() = remoteConfig.getBoolean(CAN_DELETE_POINT)

    fun isSearchV2Enabled() = remoteConfig.getBoolean(SEARCH_V2)

    sealed class Flow {
        data object SearchingPoints : Flow()
        data class Success(val points: List<SimplePoint>) : Flow()
        data object NoPoints : Flow()
        data object DeletedPoint: Flow()
        data object UpdatedPoint : Flow()
    }
}