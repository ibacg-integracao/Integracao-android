package br.com.atitude.finder.extensions

import br.com.atitude.finder.data.network.entity.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

fun Throwable.hasErrorMessage(message: List<String>) =
    (this as? HttpException)?.let {
        val errorBody: String? = it.response()?.errorBody()?.string()
        val errorResponse: ErrorResponse? = Gson().fromJson(errorBody, ErrorResponse::class.java)
        return@let message.any { message -> errorResponse?.message == message }
    } ?: false

fun Throwable.hasErrorMessage(message: String) = this.hasErrorMessage(listOf(message))

fun Throwable.hasErrorStatusCode(statusCode: Int) = (this as? HttpException)?.let {
    return@let it.code() == statusCode
} ?: false