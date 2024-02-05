package br.com.atitude.finder.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.pointdetail.PointDetail
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class DetailViewModel(appRemoteConfig: AppRemoteConfig, private val apiRepository: ApiRepository) :
    BaseViewModel(appRemoteConfig) {

    private val _pointDetail = MutableLiveData<State>(State.Loading)
    val pointDetail: LiveData<State> = _pointDetail
    fun fetchPoint(id: String) {
        _pointDetail.value = State.Loading
        launch(loadingReason = "Buscando dados da célula...") {
            _pointDetail.postValue(
                State.Success(
                    apiRepository.getPointById(id)
                )
            )
        }
    }

    sealed class State {
        data object Loading : State()
        class Success(val pointDetail: PointDetail) : State()
    }
}