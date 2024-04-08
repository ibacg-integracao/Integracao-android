package br.com.atitude.finder.presentation.authentication

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityRegisterAccountBinding
import br.com.atitude.finder.domain.Password
import br.com.atitude.finder.domain.toPassword
import br.com.atitude.finder.extensions.visibleOrGone
import br.com.atitude.finder.presentation._base.BaseActivity
import br.com.atitude.finder.presentation._base.EXTRA_EDITING_FIELD
import br.com.atitude.finder.presentation._base.EXTRA_EDITING_FIELD_EMAIL
import br.com.atitude.finder.presentation._base.EXTRA_EDITING_FIELD_NAME
import br.com.atitude.finder.presentation._base.EXTRA_EDITING_FIELD_PASSWORD
import br.com.atitude.finder.presentation._base.EXTRA_USER_ID
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

    private val userId: String? by lazy { intent.extras?.getString(EXTRA_USER_ID) }
    private val editingField: String? by lazy { intent.extras?.getString(EXTRA_EDITING_FIELD) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configInitialState()
    }

    private fun setNameFieldVisibility(visible: Boolean) {
        binding.textViewName.visibleOrGone(visible)
        binding.textInputLayoutName.visibleOrGone(visible)
    }

    private fun setEmailFieldVisibility(visible: Boolean) {
        binding.textViewEmail.visibleOrGone(visible)
        binding.textInputLayoutEmail.visibleOrGone(visible)
    }

    private fun setPasswordsFieldVisibility(visible: Boolean) {
        binding.textViewOldPassword.visibleOrGone(visible)
        binding.textViewPassword.visibleOrGone(visible)
        binding.textViewConfirmPassword.visibleOrGone(visible)
        binding.textInputPassword.visibleOrGone(visible)
        binding.textInputLayoutPassword.visibleOrGone(visible)
        binding.textInputPassword.visibleOrGone(visible)
        binding.textInputLayoutPassword.visibleOrGone(visible)
        binding.textInputLayoutOldPassword.visibleOrGone(visible)
    }

    private fun configInitialState() {
        if (isInEditMode()) {
            handleEditingModeState()
        } else {
            handleRegisteringModeState()
        }
    }

    private fun handleRegisteringModeState() {
        configPasswordField()
        configRegisterButton()
        observeEvents()
    }

    private fun handleEditingModeState() {
        setNameFieldVisibility(false)
        setEmailFieldVisibility(false)
        setPasswordsFieldVisibility(false)
        observeEvents()

        when (getEditingField()) {
            EditingField.NAME -> handleEditingNameState()
            EditingField.EMAIL -> handleEditingEmailState()
            EditingField.PASSWORD -> handleEditingPasswordState()
        }
    }

    private fun handleEditingNameState() {
        setNameFieldVisibility(true)
    }

    private fun handleEditingEmailState() {
        setEmailFieldVisibility(true)
    }

    private fun handleEditingPasswordState() {
        setPasswordsFieldVisibility(true)
        configConfirmButtonToUpdatePassword()
        configPasswordField()
    }

    private fun isInEditMode() = userId != null

    private fun getEditingField(): EditingField {
        return EditingField.entries.find { it.extra == editingField }
            ?: throw IllegalStateException("Invalid editing field extra")
    }

    override fun requireAuthentication() = isInEditMode()

    private fun observeEvents() {
        getViewModel().event.observe(this) { event ->
            when (event) {
                RegisterAccountViewModel.Event.Error -> handleErrorEvent()
                RegisterAccountViewModel.Event.RegisterUserSuccess -> handleRegisterAccountSuccessEvent()
                RegisterAccountViewModel.Event.ChangePasswordSuccess -> handleChangePasswordSuccessEvent()
                RegisterAccountViewModel.Event.EmailInUseError -> handleEmailInUseEvent()
                RegisterAccountViewModel.Event.ChangePasswordError -> handleChangePasswordErrorEvent()
                RegisterAccountViewModel.Event.WrongOldPasswordError -> handleWrongOldPassword()
            }
        }
    }

    private fun handleWrongOldPassword() {
        binding.textViewOldPassword.requestFocus()
        binding.textInputLayoutOldPassword.error = "Senha inválida"
    }

    private fun handleChangePasswordErrorEvent() {
        binding.textInputPassword.requestFocus()
        binding.textInputLayoutPassword.error = "Senha inválida"
    }

    private fun handleEmailInUseEvent() {
        binding.textInputEmail.requestFocus()
        binding.textInputLayoutEmail.error = "Email in uso"
    }

    private fun handleChangePasswordSuccessEvent() {
        Snackbar.make(binding.root, "Senha alterada com sucesso!", Snackbar.LENGTH_LONG).show()
        logout()
    }

    private fun handleRegisterAccountSuccessEvent() {
        setResult(RESULT_OK)
        finish()
    }

    private fun handleErrorEvent() {
        Snackbar.make(binding.root, "Houve um erro no cadastro da conta", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun validateName(): Boolean {
        val inputName = binding.textInputName
        val inputLayoutName = binding.textInputLayoutName
        inputLayoutName.error = null

        if (inputName.text?.toString()?.trim()?.isBlank() == true) {
            inputLayoutName.error = "Nome inválido"
            return false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        val inputEmail = binding.textInputEmail
        val inputLayoutEmail = binding.textInputLayoutEmail
        inputLayoutEmail.error = null

        if (inputEmail.text?.toString()?.trim()?.isBlank() == true) {
            inputLayoutEmail.error = "Email inválido"
            return false
        }

        return true
    }

    private fun validatePasswords(): Boolean {
        val inputPassword = binding.textInputPassword
        val inputConfirmPassword = binding.textInputConfirmPassword
        val inputLayoutPassword = binding.textInputLayoutPassword
        val inputConfirmLayoutPassword = binding.textInputLayoutConfirmPassword

        inputLayoutPassword.error = null
        inputConfirmLayoutPassword.error = null

        if (inputConfirmPassword.text?.toString() != inputPassword.text?.toString()) {
            inputConfirmLayoutPassword.error = "Senhas não coincidem."
            return false
        }

        if (!getPassword().hasMetAllConditions()) {
            inputLayoutPassword.error = "Senha inválida"
            return false
        }

        return true
    }

    private fun validateFieldsForRegister(): Boolean {
        return validateName() && validateEmail() && validatePasswords()
    }

    private fun configConfirmButtonToUpdatePassword() {
        binding.buttonRegister.setOnClickListener {
            if (validatePasswords()) {
                val inputPassword = binding.textInputPassword.text?.toString().orEmpty()
                val inputOldPassword = binding.textInputOldPassword.text?.toString().orEmpty()
                getViewModel().updatePassword("Alterando senha...", inputOldPassword, inputPassword)
            }
        }
    }

    private fun configRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            if (validateFieldsForRegister()) {
                val inputEmail = binding.textInputEmail
                val inputName = binding.textInputName
                val inputPassword = binding.textInputPassword

                val name = inputName.text?.toString().orEmpty()
                val email = inputEmail.text?.toString().orEmpty()
                val password = inputPassword.text?.toString().orEmpty()

                getViewModel().registerAccount("Registrando conta...", name, email, password)
            }
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

    enum class EditingField(val extra: String) {
        NAME(EXTRA_EDITING_FIELD_NAME),
        EMAIL(EXTRA_EDITING_FIELD_EMAIL),
        PASSWORD(EXTRA_EDITING_FIELD_PASSWORD)
    }
}