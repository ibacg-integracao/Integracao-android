package br.com.atitude.finder.presentation._base

import android.content.Context
import android.content.Intent
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation.authentication.AuthenticatorActivity
import br.com.atitude.finder.presentation.creator.CreatorActivity
import br.com.atitude.finder.presentation.map.PointMapActivity
import br.com.atitude.finder.presentation.profile.ProfileActivity
import br.com.atitude.finder.presentation.searchlist.SearchListActivity

enum class SearchType(val type: String) {
    POSTAL_CODE("postal_code"), ADDRESS("address");

    companion object {
        fun findByType(type: String): SearchType? = SearchType.values().find { it.type == type }
    }
}

fun Context.openSearchList(
    input: String?,
    type: SearchType?,
    weekDays: Set<WeekDay> = setOf(),
    tags: Set<String> = setOf(),
    times: Set<String> = setOf()
) {
    val intent = Intent(this, SearchListActivity::class.java).apply {
        putExtra(EXTRA_INPUT, input)
        putExtra(EXTRA_INPUT_TYPE, type?.type)
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

fun Context.intentPointMap(address: String?): Intent {
    return Intent(this, PointMapActivity::class.java).apply {
        address?.let { putExtra(EXTRA_ADDRESS_LINE, address) }
    }
}

fun Context.intentAuthentication(): Intent {
    return Intent(this, AuthenticatorActivity::class.java)
}

fun Context.intentProfile(): Intent {
    return Intent(this, ProfileActivity::class.java)
}