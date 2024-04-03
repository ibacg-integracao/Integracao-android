package br.com.atitude.finder.presentation.authentication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class RegisterAccountViewModel(
    appRemoteConfig: AppRemoteConfig,
    sharedPreferences: SharedPrefs,
    private val apiRepository: ApiRepository
) : BaseViewModel(
    appRemoteConfig = appRemoteConfig,
    sharedPreferences = sharedPreferences
) {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    fun registerAccount(loadingReason: String, name: String, email: String, password: String) {
        launch(loadingReason = loadingReason, apiErrorBlock = {
            when (it.message) {
                "email in use" -> _event.postValue(Event.EmailInUseError)
                else -> _event.postValue(Event.Error)
            }
        }) {
            apiRepository.registerAccount(name, email, password)
            _event.postValue(Event.Success)
        }
    }

    sealed class Event {
        data object Success : Event()
        data object Error : Event()
        data object EmailInUseError : Event()
    }
}