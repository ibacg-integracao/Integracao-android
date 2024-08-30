package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.extensions.hasErrorMessage
import br.com.atitude.finder.extensions.hasErrorStatusCode
import br.com.atitude.finder.repository.SharedPrefs
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val appRemoteConfig: AppRemoteConfig,
    private val sharedPreferences: SharedPrefs
) : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState?>()
    val errorState: LiveData<ErrorState?> = _errorState

    private val _loadingReason = MutableLiveData<String?>()
    val loadingReason: LiveData<String?> = _loadingReason

    fun hasToken(): Boolean = sharedPreferences.hasToken()

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

    open fun launch(
        loadingReason: String? = null,
        errorBlock: ((Throwable) -> Unit)? = null,
        finally: (() -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        if (loadingReason != null) {
            _loadingReason.value = loadingReason
        }

        viewModelScope.launch {
            try {
                block.invoke()
                _loadingReason.postValue(null)
            } catch (err: Throwable) {
                if (BuildConfig.DEBUG) {
                    err.printStackTrace()
                }

                if (isAuthenticationError(err)) {
                    _errorState.postValue(ErrorState.Unauthorized)
                }

                errorBlock?.invoke(err)
                _loadingReason.postValue(null)
            }
        }
    }

    private fun isAuthenticationError(err: Throwable) = err.hasErrorStatusCode(401) ||
            err.hasErrorStatusCode(403) ||
            err.hasErrorMessage(listOf("no authorization header", "Invalid token"))

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