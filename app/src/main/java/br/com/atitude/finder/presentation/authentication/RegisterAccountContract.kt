package br.com.atitude.finder.presentation.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import br.com.atitude.finder.domain.User
import br.com.atitude.finder.presentation._base.intentRegisterAccount

typealias UserEditingFieldMap = Pair<User, RegisterAccountActivity.EditingField>

class RegisterAccountContract :
    ActivityResultContract<UserEditingFieldMap?, RegisterAccountContract.Result>() {

    override fun createIntent(context: Context, input: UserEditingFieldMap?): Intent =
        context.intentRegisterAccount(input)

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        if (resultCode == Activity.RESULT_OK) return Result.OK
        return Result.FAILED
    }

    enum class Result {
        FAILED, OK;
    }
}