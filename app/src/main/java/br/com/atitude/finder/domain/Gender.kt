package br.com.atitude.finder.domain

import androidx.annotation.StringRes
import br.com.atitude.finder.R

enum class Gender(val letter: Char, @StringRes val display: Int) {
    MALE('m', R.string.male), FEMALE('f', R.string.female), UNKNOWN('?', R.string.unknown);

    companion object {
        fun getByLetter(letter: Char): Gender = values().firstOrNull { it.letter == letter } ?: UNKNOWN
    }
}