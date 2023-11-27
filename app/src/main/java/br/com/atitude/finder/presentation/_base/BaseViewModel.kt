package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseViewModel(private val appRemoteConfig: AppRemoteConfig): ViewModel() {

    private val _lastError = MutableLiveData<Throwable?>(null)
    val lastError: LiveData<Throwable?> = _lastError

    private val _apiError = MutableLiveData<ApiError?>()
    val apiError: LiveData<ApiError?> = _apiError

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

    fun setLastError(throwable: Throwable) {
        _lastError.postValue(throwable)
    }

    fun launch(showAlertOnError: Boolean = true, errorBlock: ((Throwable) -> Unit)? = null, finally: (() -> Unit)? = null, block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block.invoke()
                finally?.invoke()
            } catch (err: Throwable) {
                (err as? HttpException)?.let {
                    _apiError.postValue(ApiError(showAlertOnError, it))
                }
                errorBlock?.invoke(err)
                finally?.invoke()
            }
        }
    }

    data class ApiError(
        val showAlert: Boolean = true,
        val httpException: HttpException
    )
}