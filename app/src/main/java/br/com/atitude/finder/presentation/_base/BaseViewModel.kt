package br.com.atitude.finder.presentation._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.data.network.entity.response.ErrorResponse
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseViewModel(private val appRemoteConfig: AppRemoteConfig) : ViewModel() {

    private val _lastApiErrorMessage = MutableLiveData<String?>()
    val lastApiErrorMessage: LiveData<String?> = _lastApiErrorMessage

    private val _apiError = MutableLiveData<ApiError?>()
    val apiError: LiveData<ApiError?> = _apiError

    private val _loading = MutableLiveData<String?>()
    val loading: LiveData<String?> = _loading

    fun isOutOfOrder() = appRemoteConfig.getBoolean("OutOfOrder")

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

                    val errorBody: String? = it.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponse? =
                        Gson().fromJson(errorBody, ErrorResponse::class.java)

                    if (errorResponse != null) {
                        val message = errorResponse.message
                        _lastApiErrorMessage.postValue(message)
                    }
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