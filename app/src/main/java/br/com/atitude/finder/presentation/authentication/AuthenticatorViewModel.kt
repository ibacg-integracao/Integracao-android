package br.com.atitude.finder.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.Token
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository

class AuthenticatorViewModel(
    appRemoteConfig: AppRemoteConfig,
    private val repository: ApiRepository
) : BaseViewModel(
    appRemoteConfig
) {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun login(email: String, password: String) {
        launch(showAlertOnError = false, apiErrorBlock = {
            if (it.message == "invalid credentials" || it.message == "user not found") {
                _state.postValue(State.InvalidCredentials)
                return@launch
            }

            _state.postValue(State.Error)

        }) {
            val token: Token = repository.login(email, password)
            _state.postValue(State.Success(token))
        }
    }
}

sealed class State {
    data class Success(val token: Token) : State()
    data object Error : State()
    data object InvalidCredentials : State()
}