package br.com.atitude.finder.presentation._base

import com.google.android.material.textfield.TextInputLayout

object Validators {
    fun TextInputLayout.validatePhone() = validateField(this) { input ->
        input.trim().length == 8 || input.trim().length == 9
    }

    fun TextInputLayout.validateName() = validateField(this) { input ->
        input.split(" ").size > 1
    }

    private fun validateField(
        textInputLayout: TextInputLayout,
        message: String = "Campo inválido",
        validation: (String) -> Boolean
    ): String? {
        textInputLayout.error = null

        val editText = textInputLayout.editText ?: return null

        val editTextString = editText.text.toString()

        if(editTextString.isBlank()) {
            textInputLayout.error = "Campo vazio"
            return null
        }

        if (!validation.invoke(editTextString)) {
            textInputLayout.error = message
            return null
        }

        return editTextString
    }
}