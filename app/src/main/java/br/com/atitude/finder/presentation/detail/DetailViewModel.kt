package br.com.atitude.finder.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.pointdetail.PointDetail
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class DetailViewModel(appRemoteConfig: AppRemoteConfig, private val apiRepository: ApiRepository) :
    BaseViewModel(appRemoteConfig) {

    private val _pointDetail = MutableLiveData<State>()
    val pointDetail: LiveData<State> = _pointDetail
    fun fetchPoint(id: String) {
        launch(loadingReason = "Buscando dados da célula...", errorBlock = {
            it.printStackTrace()
        }) {
            _pointDetail.postValue(
                State.Success(
                    apiRepository.getPointById(id)
                )
            )
        }
    }

    sealed class State {
        class Success(val pointDetail: PointDetail) : State()
    }
}