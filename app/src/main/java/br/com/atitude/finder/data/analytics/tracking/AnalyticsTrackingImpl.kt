package br.com.atitude.finder.data.analytics.tracking

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class AnalyticsTrackingImpl(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsTracking {
    override fun log(event: Event) {
        firebaseAnalytics.logEvent(event.name, event.toBundle())
    }
}