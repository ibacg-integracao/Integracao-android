package br.com.atitude.finder.data.analytics.tracking

import androidx.core.os.bundleOf
import br.com.atitude.finder.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.google.firebase.analytics.logEvent

class AnalyticsTrackingImpl(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsTracking {
    override fun log(event: Event) {
        firebaseAnalytics.logEvent(event.name, event.toBundle())
    }

    override fun log(name: String, builder: ParametersBuilder.() -> Unit) {
        firebaseAnalytics.setDefaultEventParameters(bundleOf().apply {
            putString("environment", BuildConfig.BUILD_TYPE)
        })
        firebaseAnalytics.logEvent(name) { builder() }
    }
}