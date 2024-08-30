package br.com.atitude.finder.presentation.authentication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.network.entity.Errors.EMAIL_IN_USER
import br.com.atitude.finder.data.network.entity.Errors.WRONG_PASSWORD
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.extensions.hasErrorMessage
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
        launch(loadingReason = loadingReason, errorBlock = {
            when {
                it.hasErrorMessage(EMAIL_IN_USER) -> _event.postValue(Event.EmailInUseError)
                else -> _event.postValue(Event.Error)
            }
        }) {
            apiRepository.registerAccount(name, email, password)
            _event.postValue(Event.RegisterUserSuccess)
        }
    }

    fun updatePassword(loadingReason: String, oldPassword: String, newPassword: String) {
        launch(loadingReason = loadingReason, errorBlock = {
            when {
                it.hasErrorMessage(WRONG_PASSWORD) -> _event.postValue(Event.WrongOldPasswordError)
                else -> Event.ChangePasswordError
            }
        }) {
            apiRepository.changePassword(oldPassword, newPassword)
            _event.postValue(Event.ChangePasswordSuccess)
        }
    }

    sealed class Event {
        data object RegisterUserSuccess : Event()
        data object ChangePasswordSuccess : Event()
        data object Error : Event()
        data object EmailInUseError : Event()
        data object ChangePasswordError : Event()
        data object WrongOldPasswordError : Event()
    }
}