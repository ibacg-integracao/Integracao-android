package br.com.atitude.finder.presentation.authentication.passwordcondition

const val VALID_SYMBOLS = "!@#$%&*()-_=+/.,:;|?[]{}"

class HasSymbol: PasswordCondition() {
    override fun validate(input: String) = input.any { VALID_SYMBOLS.contains(it) }
}