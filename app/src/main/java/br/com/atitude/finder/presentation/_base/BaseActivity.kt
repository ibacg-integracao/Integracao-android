package br.com.atitude.finder.presentation._base

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.atitude.finder.data.network.entity.ErrorResponse
import com.google.gson.Gson


abstract class BaseActivity: AppCompatActivity() {
    abstract fun getViewModel(): BaseViewModel?

    fun configApiErrorHandler() {
        getViewModel()?.apiError?.observe(this) { error ->

            if(error != null) {

                if(error.showAlert) {
                    val errorBody: String? = error.httpException.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponse? = Gson().fromJson(errorBody, ErrorResponse::class.java)

                    if(errorResponse != null) {
                        val message = errorResponse.message

                        AlertDialog.Builder(this)
                            .setTitle("Erro:")
                            .setMessage(message)
                            .create()
                            .show()
                    }
                }
            }
        }
    }
}