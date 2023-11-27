package br.com.atitude.finder.di

import android.location.Geocoder
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTrackingImpl
import br.com.atitude.finder.data.network.NetworkApi
import br.com.atitude.finder.data.network.RetrofitConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.ApiRepositoryImpl
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.Locale

val dataModules = module {
    single {
        RetrofitConfig.instantiate().create(NetworkApi::class.java)
    }

    single {
        Geocoder(androidContext(), Locale("pt_br", "Brazil"))
    }

    single {
        FirebaseAnalytics.getInstance(androidContext())
    }

    single { AppRemoteConfig() }

    factory<AnalyticsTracking> { AnalyticsTrackingImpl(get()) }
    factory<ApiRepository> { ApiRepositoryImpl(get()) }
}