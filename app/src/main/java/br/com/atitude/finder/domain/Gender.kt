package br.com.atitude.finder.domain

import androidx.annotation.StringRes
import br.com.atitude.finder.R

enum class Gender(val letter: String, @StringRes val display: Int) {
    MALE("m", R.string.male), FEMALE("f", R.string.female);

    companion object {
        fun getByLetter(letter: String): Gender? = values().firstOrNull { it.letter == letter }
    }
}