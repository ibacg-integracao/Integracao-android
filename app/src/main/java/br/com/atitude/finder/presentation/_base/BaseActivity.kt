package br.com.atitude.finder.presentation._base

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.atitude.finder.R


abstract class BaseActivity : AppCompatActivity() {
    abstract fun getViewModel(): BaseViewModel?

    private var successLoginBehaviour: (() -> Unit)? = null

    private val loginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) successLoginBehaviour?.invoke()
        }

    fun setSuccessLoginBehaviour(callback: () -> Unit) {
        successLoginBehaviour = callback
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

    open fun configApiErrorHandler() {
        getViewModel()?.errorState?.observe(this) { errorState ->
            if (errorState != null) handleError(errorState)

        }
    }

    private fun handleError(errorState: BaseViewModel.ErrorState) {
        when (errorState) {
            is BaseViewModel.ErrorState.Unauthorized -> handleUnauthorizedOrForbiddenError()
            is BaseViewModel.ErrorState.Generic -> handleGenericError(errorState)
        }
    }

    private fun handleUnauthorizedOrForbiddenError() {
        if (successLoginBehaviour != null)
            loginResult.launch(intentAuthentication())
    }

    private fun handleGenericError(error: BaseViewModel.ErrorState.Generic) {
        AlertDialog.Builder(this)
            .setTitle("Erro sem tratativa")
            .setMessage("${error.message}-${error.statusCode}")
            .show()
    }

    private fun handleUnknownError() {
        AlertDialog.Builder(this)
            .setTitle("Erro desconhecido")
            .setMessage("Ocorreu um erro e não foi possível identificá-lo. Relate ao administrador.")
            .show()
    }
}