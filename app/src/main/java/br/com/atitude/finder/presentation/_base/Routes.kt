package br.com.atitude.finder.presentation._base

import android.content.Context
import android.content.Intent
import br.com.atitude.finder.domain.User
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.presentation.authentication.AuthenticatorActivity
import br.com.atitude.finder.presentation.authentication.RegisterAccountActivity
import br.com.atitude.finder.presentation.creator.CreatorActivity
import br.com.atitude.finder.presentation.detail.PointDetailActivity
import br.com.atitude.finder.presentation.profile.ProfileActivity
import br.com.atitude.finder.presentation.search.SearchActivity
import br.com.atitude.finder.presentation.searchlist.SearchListActivity
import br.com.atitude.finder.presentation.users.UsersManagerActivity

fun Context.openPointDetail(pointId: String) {
    val intent = Intent(this, PointDetailActivity::class.java).apply {
        putExtra(EXTRA_POINT_ID, pointId)
    }
    startActivity(intent)
}

fun Context.intentHome() = Intent(this, SearchActivity::class.java)

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

fun Context.intentAuthentication(): Intent {
    return Intent(this, AuthenticatorActivity::class.java)
}

fun Context.intentProfile(): Intent {
    return Intent(this, ProfileActivity::class.java)
}

fun Context.intentRegisterAccount(userIdFieldChange: Pair<User, RegisterAccountActivity.EditingField>? = null): Intent {
    return Intent(this, RegisterAccountActivity::class.java).apply {
        userIdFieldChange?.let {
            putExtra(EXTRA_USER_ID, it.first.id)
            putExtra(EXTRA_EDITING_FIELD, it.second.extra)
        }
    }
}

fun Context.intentUsersManager() = Intent(this, UsersManagerActivity::class.java)