package br.com.atitude.finder.presentation.authentication

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import br.com.atitude.finder.presentation._base.intentAuthentication

class AuthenticatorContract : ActivityResultContract<Any?, AuthenticatorContract.Result>() {
    override fun createIntent(context: Context, input: Any?): Intent {
        return context.intentAuthentication()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        if (resultCode == RESULT_OK) return Result.OK
        return Result.FAILED
    }

    enum class Result {
        FAILED, OK;
    }

}