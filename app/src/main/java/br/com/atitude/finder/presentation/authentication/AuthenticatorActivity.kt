package br.com.atitude.finder.presentation.authentication

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import br.com.atitude.finder.databinding.ActivityAuthenticatorBinding
import br.com.atitude.finder.presentation._base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticatorActivity : BaseActivity() {

    private lateinit var binding: ActivityAuthenticatorBinding

    private val authenticationViewModel: AuthenticatorViewModel by viewModel()
    override fun getViewModel() = authenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.textInputEmail.text?.toString().orEmpty()
            val password = binding.textInputPassword.text?.toString().orEmpty()

            tryLogin(email, password)
        }
    }

    private fun tryLogin(email: String, password: String) {
        getViewModel().login(email, password)
    }

    override fun onStart() {
        super.onStart()

        getViewModel().state.observe(this) { state ->
            when(state) {
                is State.InvalidCredentials -> handleInvalidCredentials()
                is State.Success -> handleSuccess()
                else -> Unit
            }
        }
    }

    private fun handleSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    private fun handleInvalidCredentials() {
        AlertDialog.Builder(this)
            .setMessage("Credenciais inválidas")
            .show()
    }
}