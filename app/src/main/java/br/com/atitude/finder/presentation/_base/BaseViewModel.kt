package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.repository.SharedPrefs
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val appRemoteConfig: AppRemoteConfig,
    private val sharedPreferences: SharedPrefs
) : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState?>()
    val errorState: LiveData<ErrorState?> = _errorState
    private val _lastApiErrorMessage = MutableLiveData<String?>()
    val lastApiErrorMessage: LiveData<String?> = _lastApiErrorMessage

    private val _apiError = MutableLiveData<ApiError?>()
    val apiError: LiveData<ApiError?> = _apiError

    private val _loadingReason = MutableLiveData<String?>()
    val loadingReason: LiveData<String?> = _loadingReason

    fun hasToken(): Boolean = sharedPreferences.hasToken()

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

    open fun launch(
        loadingReason: String? = null,
        errorBlock: ((Throwable) -> Unit)? = null,
        apiErrorBlock: ((BackendFriendlyError) -> Unit)? = null,
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
                if (BuildConfig.DEBUG)
                    err.printStackTrace()
                errorBlock?.invoke(err)
                _loadingReason.postValue(null)
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