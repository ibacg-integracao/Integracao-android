package br.com.atitude.finder.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.Token
import br.com.atitude.finder.presentation._base.BaseViewModel
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.SharedPrefs

class AuthenticatorViewModel(
    appRemoteConfig: AppRemoteConfig,
    private val repository: ApiRepository,
    private val sharedPrefs: SharedPrefs,
) : BaseViewModel(
    appRemoteConfig,
    sharedPrefs
) {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private fun onFailedLogin(backendFriendlyError: BackendFriendlyError) {
        if (backendFriendlyError.message == "invalid credentials" || backendFriendlyError.message == "user not found") {
            _state.postValue(State.InvalidCredentials)
            return
        }

        _state.postValue(State.Error)
    }

    fun login(email: String, password: String) {
        launch(showAlertOnError = false, apiErrorBlock = { onFailedLogin(it) }) {
            val token: Token = repository.login(email, password)
            sharedPrefs.setToken(token.token)
            _state.postValue(State.Success(token))
        }
    }
}

sealed class State {
    data class Success(val token: Token) : State()
    data object Error : State()
    data object InvalidCredentials : State()
}