package br.com.atitude.finder.presentation._base

import android.content.Context
import android.content.Intent
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation.creator.CreatorActivity
import br.com.atitude.finder.presentation.detail.PointDetailActivity
import br.com.atitude.finder.presentation.searchlist.SearchListActivity

fun Context.openPointDetail(pointId: String) {
    val intent = Intent(this, PointDetailActivity::class.java).apply {
        putExtra(EXTRA_POINT_ID, pointId)
    }
    startActivity(intent)
}

fun Context.openSearchList(
    input: String?,
    weekDays: Set<WeekDay> = setOf(),
    tags: Set<String> = setOf(),
    times: Set<String> = setOf()
) {
    val intent = Intent(this, SearchListActivity::class.java).apply {
        putExtra(EXTRA_INPUT, input)
        putExtra(EXTRA_WEEK_DAYS, weekDays.map { it.response }.toTypedArray())
        putExtra(EXTRA_TAGS, tags.toTypedArray())
        putExtra(EXTRA_TIMES, times.toTypedArray())
    }
    startActivity(intent)
}

fun Context.openCreator() {
    val intent = Intent(this, CreatorActivity::class.java)
    startActivity(intent)
}