package br.com.atitude.finder.presentation.authentication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityAuthenticatorBinding
import br.com.atitude.finder.presentation._base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticatorActivity : BaseActivity() {

    private lateinit var binding: ActivityAuthenticatorBinding

    private val authenticationViewModel: AuthenticatorViewModel by viewModel()
    override fun getViewModel() = authenticationViewModel

    private var lastToast: Toast? = null

    private var registerAccountContract = registerForActivityResult(RegisterAccountContract()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configEmailPasswordChangeListener()
        configLoginButton()
        configOnBackPressedDispatcher()
        configRegisterText()
    }

    private fun configRegisterText() {
        binding.textViewRegisterAccount.setOnClickListener {
            registerAccountContract.launch(null)
        }
    }

    private fun configOnBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback {
            showNeedToBeLoggedToast()
            this.handleOnBackCancelled()
        }
    }

    private fun configLoginButton() {
        binding.buttonLogin.setOnClickListener {
            handleAccessButtonAction()
        }
    }

    private fun showNeedToBeLoggedToast() {
        val lastToast = lastToast
        if (lastToast == null) {
            val newToast = Toast.makeText(
                this@AuthenticatorActivity,
                getString(R.string.need_to_be_logged),
                Toast.LENGTH_LONG
            )
            this@AuthenticatorActivity.lastToast = newToast
            newToast.show()
        } else {
            lastToast.cancel()
            this@AuthenticatorActivity.lastToast = null
        }
    }

    override fun requireAuthentication() = false

    private fun configEmailPasswordChangeListener() {
        binding.textInputEmail.addTextChangedListener {
            onEmailOrPasswordFieldChange()
        }

        binding.textInputPassword.addTextChangedListener {
            onEmailOrPasswordFieldChange()
        }
    }

    private fun onEmailOrPasswordFieldChange() {
        binding.buttonLogin.isEnabled = canTryAccess()
    }

    private fun canTryAccess(): Boolean {
        val email = binding.textInputEmail.text?.toString().orEmpty()
        val password = binding.textInputPassword.text?.toString().orEmpty()

        return !(email.isBlank() || password.isBlank())
    }

    private fun handleAccessButtonAction() {
        val email = binding.textInputEmail.text?.toString().orEmpty()
        val password = binding.textInputPassword.text?.toString().orEmpty()
        tryLogin(email, password)
    }

    private fun tryLogin(email: String, password: String) {
        binding.buttonLogin.isEnabled = false
        getViewModel().login(email, password)
    }

    override fun onStart() {
        super.onStart()

        getViewModel().state.observe(this) { state ->
            when (state) {
                is State.InvalidCredentialsError -> handleInvalidCredentials()
                is State.Success -> handleSuccess()
                is State.UserNotAcceptedError -> handleUserNotAcceptedError()
                else -> binding.buttonLogin.isEnabled = true
            }
        }
    }

    private fun handleUserNotAcceptedError() {
        Snackbar.make(binding.root, "Esse usuário não tem acesso ao sistema", Snackbar.LENGTH_LONG).show()
    }

    private fun handleSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    private fun handleInvalidCredentials() {
        Snackbar.make(binding.root, "Credenciais inválidas", Snackbar.LENGTH_LONG).show()
    }
}