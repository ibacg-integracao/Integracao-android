package br.com.atitude.finder.domain

data class SearchParams(
    val weekDays: List<String>,
    val times: List<String>,
    val tags: List<String>
) {
    fun toWeekDays(): List<WeekDay> {
        return weekDays.mapNotNull { WeekDay.getByResponse(it) }
    }
}