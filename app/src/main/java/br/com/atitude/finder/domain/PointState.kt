package br.com.atitude.finder.domain

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import br.com.atitude.finder.R

enum class PointState(val label: String, @StringRes val message: Int) {
    ACTIVE("active", R.string.point_state_active),
    INACTIVE(
        "inactive",
        R.string.point_state_inactive
    ),
    SUSPENDED("suspended", R.string.point_state_suspended), UNKNOWN(
        "unknown",
        R.string.point_state_unknown
    );

    companion object {
        fun getByLabel(label: String) = PointState.entries.find { it.label == label } ?: UNKNOWN
    }
}