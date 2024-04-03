package br.com.atitude.finder.presentation.authentication.passwordcondition

class HasLowercaseLetter: PasswordCondition() {
    override fun validate(input: String) = input.any { it.isLowerCase() }
}