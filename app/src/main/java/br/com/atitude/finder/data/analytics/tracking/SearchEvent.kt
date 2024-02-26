package br.com.atitude.finder.data.analytics.tracking

import android.os.Bundle
import androidx.core.os.bundleOf
import br.com.atitude.finder.domain.WeekDay

class SearchEvent(
    private val postalCode: String?,
    private val weekDays: List<WeekDay> = emptyList(),
    private val categories: List<String> = emptyList(),
    private val times: List<String> = emptyList()
) : Event(name = "search_points") {
    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString("postal_code", postalCode)
            putBundle("week_days", bundleOf().apply {
                weekDays.forEach { weekDay -> this.putString("week_day", weekDay.name) }
            })
            putBundle("categories", bundleOf().apply {
                categories.forEach { category -> this.putString("category", category) }
            })
            putBundle("times", bundleOf().apply {
                times.forEach { time -> this.putString("time", time) }
            })
        }
    }

}