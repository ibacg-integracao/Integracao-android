package br.com.atitude.finder.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.atitude.finder.data.network.entity.Errors.INVALID_CREDENTIALS
import br.com.atitude.finder.data.network.entity.Errors.INVALID_PASSWORD
import br.com.atitude.finder.data.network.entity.Errors.USER_NOT_ACCEPTED
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.domain.Token
import br.com.atitude.finder.extensions.hasErrorMessage
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

    fun login(email: String, password: String) {
        launch(
            errorBlock = {
                if (it.hasErrorMessage(
                        listOf(
                            INVALID_CREDENTIALS,
                            INVALID_PASSWORD
                        )
                    )
                ) {
                    _state.postValue(State.InvalidCredentialsError)
                } else if (it.hasErrorMessage(USER_NOT_ACCEPTED)) {
                    _state.postValue(State.UserNotAcceptedError)
                } else {
                    _state.postValue(State.Error)
                }

            }) {
            val token: Token = repository.login(email, password)
            sharedPrefs.setToken(token.token)
            _state.postValue(State.Success(token))
        }
    }
}

sealed class State {
    data class Success(val token: Token) : State()
    data object Error : State()
    data object InvalidCredentialsError : State()
    data object UserNotAcceptedError : State()
}