package br.com.atitude.finder.presentation.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.network.entity.Errors
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.data.remoteconfig.Constants.CAN_DELETE_POINT
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.extensions.hasErrorMessage
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class SearchListViewModel(
    private val repository: ApiRepository,
    private val remoteConfig: AppRemoteConfig,
    private val analyticsTracking: AnalyticsTracking,
    sharedPreferences: SharedPrefs
) : BaseViewModel(remoteConfig, sharedPreferences) {

    private val _flow = MutableLiveData<Flow>(Flow.Loading)
    val flow: LiveData<Flow> = _flow

    fun trackSearchPointName(pointName: String) {
        analyticsTracking.log("search_point_name") {
            param("value", pointName)
        }
    }

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

    fun deletePoint(loadingReason: String, id: String) {
        launch(loadingReason = loadingReason) {
            repository.deletePoint(id)
            _flow.postValue(Flow.DeletedPoint)
        }
    }

    fun searchByAddressOrPostalCode(
        input: String,
        pointName: String?,
        weekDays: List<String> = emptyList(),
        tags: List<String> = emptyList(),
        times: List<String>
    ) {
        _flow.postValue(Flow.Loading)
        launch(errorBlock = {
            if(it.hasErrorMessage(Errors.NOT_FOUND_ADDRESS_OR_POSTAL_CODE)) {
                _flow.postValue(Flow.AddressOrPostalCodeNotFound)
                return@launch
            }
            _flow.postValue(Flow.Error)
        }) {
            val points =
                repository.searchPointsByAddressOrPostalCode(
                    input = input,
                    pointName = pointName,
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

    sealed class Flow {
        data object Loading : Flow()
        data class Success(val points: List<SimplePoint>) : Flow()
        data object NoPoints : Flow()
        data object DeletedPoint : Flow()
        data object UpdatedPoint : Flow()
        data object Error : Flow()
        data object AddressOrPostalCodeNotFound : Flow()
    }
}