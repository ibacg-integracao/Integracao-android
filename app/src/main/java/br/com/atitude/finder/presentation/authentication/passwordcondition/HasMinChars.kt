package br.com.atitude.finder.presentation.authentication.passwordcondition

class HasMinChars(private val minCount: Int) : PasswordCondition() {
    override fun validate(input: String) = input.length >= minCount
}