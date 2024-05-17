package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.extensions.isAuthenticationError
import br.com.atitude.finder.extensions.toBackendFriendlyError
import br.com.atitude.finder.repository.SharedPrefs
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseViewModel(
    private val appRemoteConfig: AppRemoteConfig,
    private val sharedPreferences: SharedPrefs
) : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState?>()
    val errorState: LiveData<ErrorState?> = _errorState
    private val _lastApiErrorMessage = MutableLiveData<String?>()
    val lastApiErrorMessage: LiveData<String?> = _lastApiErrorMessage

    private val _loading = MutableLiveData<String?>()
    val loading: LiveData<String?> = _loading

    fun hasToken(): Boolean = sharedPreferences.hasToken()

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

    open fun launch(
        loadingReason: String? = null,
        showAlertOnError: Boolean = true,
        errorBlock: ((Throwable) -> Unit)? = null,
        apiErrorBlock: ((BackendFriendlyError) -> Unit)? = null,
        finally: (() -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        if (loadingReason != null) {
            _loading.value = loadingReason
        }

        viewModelScope.launch {
            try {
                block.invoke()
                _loading.postValue(null)
            } catch (err: Throwable) {
                if (BuildConfig.DEBUG)
                    err.printStackTrace()

                (err as? HttpException)?.let {

                    err.toBackendFriendlyError()?.let { backendFriendlyError ->
                        if (backendFriendlyError.isAuthenticationError()) {
                            _errorState.postValue(ErrorState.Unauthorized)
                        } else {
                            _errorState.postValue(
                                ErrorState.Generic(
                                    backendFriendlyError.message,
                                    backendFriendlyError.statusCode
                                )
                            )
                        }

                        apiErrorBlock?.invoke(backendFriendlyError)
                    }
                }
                errorBlock?.invoke(err)
                _loading.postValue(null)
            }
        }
    }

    fun logout() {
        sharedPreferences.clearToken()
    }

    sealed class ErrorState {
        data object Unauthorized : ErrorState()
        data class Generic(val message: String, val statusCode: Int) : ErrorState()
    }

    data class BackendFriendlyError(
        val statusCode: Int,
        val message: String
    )
}