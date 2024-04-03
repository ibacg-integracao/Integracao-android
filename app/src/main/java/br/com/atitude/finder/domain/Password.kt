package br.com.atitude.finder.domain

import br.com.atitude.finder.presentation.authentication.passwordcondition.HasLowercaseLetter
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasMinChars
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasNumber
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasSymbol
import br.com.atitude.finder.presentation.authentication.passwordcondition.HasUppercaseLetter

data class Password(
    val value: String
) {
    private val passwordConditions =
        listOf(HasLowercaseLetter(), HasUppercaseLetter(), HasMinChars(8), HasSymbol(), HasNumber())

    private fun getMetConditions() = passwordConditions.filter { it.validate(value) }

    fun hasMetAllConditions() = getMetConditions().size == passwordConditions.size
}

fun String.toPassword(): Password = Password(this)