package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.extensions.toBackendFriendlyError
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseViewModel(private val appRemoteConfig: AppRemoteConfig) : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState?>()
    val errorState: LiveData<ErrorState?> = _errorState

    private val _loading = MutableLiveData<String?>()
    val loading: LiveData<String?> = _loading

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
                if(BuildConfig.DEBUG)
                    err.printStackTrace()

                (err as? HttpException)?.let {
                    err.toBackendFriendlyError()?.let { backendFriendlyError ->

                        when(backendFriendlyError.statusCode) {
                            401, 403 -> _errorState.postValue(ErrorState.Unauthorized)
                            else -> _errorState.postValue(ErrorState.Generic(backendFriendlyError.message, backendFriendlyError.statusCode))
                        }

                        apiErrorBlock?.invoke(backendFriendlyError)
                    }
                }
                errorBlock?.invoke(err)
                _loading.postValue(null)
            }
        }
    }

    sealed  class ErrorState {
        data object Unauthorized : ErrorState()
        data class Generic(val message: String, val statusCode: Int) : ErrorState()
    }

    data class BackendFriendlyError(
        val statusCode: Int,
        val message: String
    )
}