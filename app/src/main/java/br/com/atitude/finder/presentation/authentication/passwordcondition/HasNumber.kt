package br.com.atitude.finder.presentation.authentication.passwordcondition

class HasNumber: PasswordCondition() {
    override fun validate(input: String) = input.any { it.isDigit() }
}