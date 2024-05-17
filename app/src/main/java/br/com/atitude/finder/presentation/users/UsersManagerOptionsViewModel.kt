package br.com.atitude.finder.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class UsersManagerOptionsViewModel(
    appRemoteConfig: AppRemoteConfig,
    sharedPreferences: SharedPrefs,
    private val apiRepository: ApiRepository
) : BaseViewModel(appRemoteConfig, sharedPreferences) {

    private val _event = MutableLiveData<Event?>()
    val event: LiveData<Event?> = _event

    fun reset() {
        _event.postValue(null)
    }

    fun enableOrDisableUser(userId: String, enable: Boolean) {
        _event.postValue(Event.LoadingEnableOrDisableEvent)

        launch(showAlertOnError = false, finally = { _event.postValue(null) }) {
            if (enable) apiRepository.enableUser(userId)
            else apiRepository.disableUser(userId)

            _event.postValue(Event.EnabledOrDisabledUser)
        }
    }

    fun deleteUser(userId: String) {
        _event.postValue(Event.LoadingDeleteUser)

        launch(finally = { _event.postValue(null) }) {
            apiRepository.deleteUser(userId)
            _event.postValue(Event.DeletedUser)
        }
    }

    sealed class Event {

        data object LoadingEnableOrDisableEvent : Event()
        data object LoadingDeleteUser : Event()
        data object EnabledOrDisabledUser : Event()
        data object DeletedUser : Event()
    }
}