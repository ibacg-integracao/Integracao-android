package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseViewModel(private val appRemoteConfig: AppRemoteConfig) : ViewModel() {

    private val _lastError = MutableLiveData<Throwable?>(null)
    val lastError: LiveData<Throwable?> = _lastError

    private val _apiError = MutableLiveData<ApiError?>()
    val apiError: LiveData<ApiError?> = _apiError

    private val _loading = MutableLiveData<String?>()
    val loading: LiveData<String?> = _loading

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

    fun setLastError(throwable: Throwable) {
        _lastError.postValue(throwable)
    }

    fun launch(
        loadingReason: String? = null,
        showAlertOnError: Boolean = true,
        errorBlock: ((Throwable) -> Unit)? = null,
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
                    _apiError.postValue(ApiError(showAlertOnError, it))
                }
                errorBlock?.invoke(err)
                _loading.postValue(null)
            }
        }
    }

    data class ApiError(
        val showAlert: Boolean = true,
        val httpException: HttpException
    )
}