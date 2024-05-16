package br.com.atitude.finder.data.analytics.tracking

import com.google.firebase.analytics.ParametersBuilder

interface AnalyticsTracking {
    fun log(event: Event)
    fun log(name: String, builder: ParametersBuilder.() -> Unit = {})
}