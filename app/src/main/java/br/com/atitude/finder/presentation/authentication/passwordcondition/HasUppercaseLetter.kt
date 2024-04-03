package br.com.atitude.finder.presentation.authentication.passwordcondition

class HasUppercaseLetter: PasswordCondition() {
    override fun validate(input: String) = input.any { it.isUpperCase() }
}