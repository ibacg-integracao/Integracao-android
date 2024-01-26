package br.com.atitude.finder.presentation._base

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.atitude.finder.R
import br.com.atitude.finder.data.network.entity.ErrorResponse
import com.google.gson.Gson


abstract class BaseActivity : AppCompatActivity() {
    abstract fun getViewModel(): BaseViewModel?
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel()?.let { viewModel ->
            viewModel.loading.observe(this) { reason ->
                if (reason == null) {
                    progressDialog?.dismiss()
                } else {
                    progressDialog = ProgressDialog.show(this@BaseActivity, null, reason, true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        if (getViewModel()?.isOutOfOrder() == true) {
            val title = getString(R.string.out_of_order_title)
            val message = getString(R.string.out_of_order_message)
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .show()
        }
    }

    fun configApiErrorHandler() {
        getViewModel()?.apiError?.observe(this) { error ->

            if (error != null) {

                if (error.showAlert) {
                    val errorBody: String? = error.httpException.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponse? =
                        Gson().fromJson(errorBody, ErrorResponse::class.java)

                    if (errorResponse != null) {
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