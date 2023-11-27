package br.com.atitude.finder.data.analytics.tracking

interface AnalyticsTracking {
    fun log(event: Event)
}