package br.com.atitude.finder.presentation.authentication.passwordcondition

abstract class PasswordCondition {
    abstract fun validate(input: String): Boolean
}