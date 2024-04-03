package br.com.atitude.finder.presentation.authentication

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityRegisterAccountBinding
import br.com.atitude.finder.domain.Password
import br.com.atitude.finder.domain.toPassword
import br.com.atitude.finder.presentation._base.BaseActivity
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasLowercaseLetter
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasMinChars
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasNumber
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasSymbol
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasUppercaseLetter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterAccountActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterAccountBinding

    private val registerAccountViewModel: RegisterAccountViewModel by viewModel()
    override fun getViewModel() = registerAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configPasswordField()
        configRegisterButton()
        observeEvent()
    }

    override fun requireAuthentication() = false

    private fun observeEvent() {
        getViewModel().event.observe(this) { event ->
            when (event) {
                RegisterAccountViewModel.Event.Error -> handleErrorEvent()
                RegisterAccountViewModel.Event.Success -> handleSuccessEvent()
                RegisterAccountViewModel.Event.EmailInUseError -> handleEmailInUseEvent()
            }
        }
    }

    private fun handleEmailInUseEvent() {
        binding.textInputEmail.requestFocus()
        binding.textInputLayoutEmail.error = "Email in uso"
    }

    private fun handleSuccessEvent() {
        setResult(RESULT_OK)
        finish()
    }

    private fun handleErrorEvent() {
        Snackbar.make(binding.root, "Houve um erro no cadastro da conta", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun configRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            val inputEmail = binding.textInputEmail
            val inputName = binding.textInputName
            val inputPassword = binding.textInputPassword
            val inputConfirmPassword = binding.textInputConfirmPassword

            val inputLayoutEmail = binding.textInputLayoutEmail
            val inputLayoutName = binding.textInputLayoutName
            val inputLayoutPassword = binding.textInputLayoutPassword
            val inputConfirmLayoutPassword = binding.textInputLayoutConfirmPassword

            inputLayoutName.error = null
            inputLayoutEmail.error = null
            inputLayoutPassword.error = null
            inputConfirmLayoutPassword.error = null

            if (inputName.text?.toString()?.trim()?.isBlank() == true) {
                inputLayoutPassword.error = "Nome inválido"
                return@setOnClickListener
            }

            if (inputEmail.text?.toString()?.trim()?.isBlank() == true) {
                inputLayoutEmail.error = "Email inválido"
                return@setOnClickListener
            }

            if (inputConfirmPassword.text?.toString() != inputPassword.text?.toString()) {
                inputConfirmLayoutPassword.error = "Senhas não coincidem."
                return@setOnClickListener
            }

            if (!getPassword().hasMetAllConditions()) {
                inputLayoutPassword.error = "Senha inválida"
                return@setOnClickListener
            }

            val name = inputName.text?.toString().orEmpty()
            val email = inputEmail.text?.toString().orEmpty()
            val password = inputPassword.text?.toString().orEmpty()

            getViewModel().registerAccount("Registrando conta...", name, email, password)
        }
    }

    private fun getPassword(): Password =
        binding.textInputPassword.text?.toString()?.toPassword()
            ?: throw IllegalArgumentException("no text in input")

    private fun getTextViewPasswordConditionMap() = mapOf(
        binding.textViewHasLowercaseLetter to HasLowercaseLetter(),
        binding.textViewHasUppercaseLetter to HasUppercaseLetter(),
        binding.textViewHasNumber to HasNumber(),
        binding.textViewHasSymbol to HasSymbol(),
        binding.textViewHasMinCount to HasMinChars(8),
    )

    private fun getPasswordConditionTextViewMap() =
        getTextViewPasswordConditionMap().map { Pair(it.value, it.key) }.toMap()

    private fun configPasswordField() {
        binding.textInputPassword.doAfterTextChanged { nullableEditable ->
            nullableEditable?.let { editable ->
                var conditionsMet = 0
                getPasswordConditionTextViewMap().entries.forEach {
                    val passwordCondition = it.key
                    val textView = it.value

                    if (passwordCondition.validate(editable.toString())) {
                        TextViewCompat.setCompoundDrawableTintList(
                            textView,
                            ColorStateList.valueOf(getColor(R.color.green))
                        )
                        textView.setTextColor(getColor(R.color.green))
                        conditionsMet++
                    } else {
                        TextViewCompat.setCompoundDrawableTintList(
                            textView,
                            ColorStateList.valueOf(getColor(R.color.red))
                        )
                        textView.setTextColor(getColor(R.color.red))
                    }
                }

                binding.buttonRegister.isEnabled =
                    conditionsMet == getPasswordConditionTextViewMap().size
            }
        }
    }
}