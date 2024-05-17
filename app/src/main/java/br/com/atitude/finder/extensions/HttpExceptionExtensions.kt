package br.com.atitude.finder.extensions

import br.com.atitude.finder.data.network.entity.response.ErrorResponse
import br.com.atitude.finder.presentation._base.BaseViewModel
import com.google.gson.Gson
import retrofit2.HttpException

fun HttpException.toBackendFriendlyError(): BaseViewModel.BackendFriendlyError? {

    try {
        val errorBody: String = this.response()?.errorBody()?.string() ?: return null

        val errorResponse: ErrorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            ?: return null

        val message = errorResponse.message
        return BaseViewModel.BackendFriendlyError(this.code(), message)
    } catch (ex: Exception) {
        return null
    }
}

fun BaseViewModel.BackendFriendlyError.isAuthenticationError(): Boolean {
    return this.statusCode == 401 || this.statusCode == 403 || this.message == "Invalid token" || this.message == "no authorization header"
}