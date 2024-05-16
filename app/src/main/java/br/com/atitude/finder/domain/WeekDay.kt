package br.com.atitude.finder.domain

import androidx.annotation.StringRes
import br.com.atitude.finder.R

enum class WeekDay(val response: String, @StringRes val localization: Int) {
    MONDAY("monday", R.string.monday),
    TUESDAY("tuesday", R.string.tuesday),
    WEDNESDAY("wednesday", R.string.wednesday),
    THURSDAY("thursday", R.string.thursday),
    FRIDAY("friday", R.string.friday),
    SATURDAY("saturday", R.string.saturday),
    UNKNOWN("", R.string.unknown);

    companion object {
        fun getByResponse(response: String): WeekDay =
            WeekDay.values().find { it.response == response } ?: UNKNOWN
    }
}