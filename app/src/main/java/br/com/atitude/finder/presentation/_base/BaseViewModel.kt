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

    private val _apiError = MutableLiveData<ApiError?>()
    val apiError: LiveData<ApiError?> = _apiError

    private val _loadingReason = MutableLiveData<String?>()
    val loadingReason: LiveData<String?> = _loadingReason

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

    fun launch(
        loadingReason: String? = null,
        errorBlock: ((Throwable) -> Unit)? = null,
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
                if(BuildConfig.DEBUG)
                    err.printStackTrace()
                errorBlock?.invoke(err)
                _loadingReason.postValue(null)
            }
        }
    }

    data class ApiError(
        val showAlert: Boolean = true,
        val httpException: HttpException
    )
}