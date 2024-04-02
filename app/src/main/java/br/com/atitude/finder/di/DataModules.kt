package br.com.atitude.finder.di

import android.location.Geocoder
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTracking
import br.com.atitude.finder.data.analytics.tracking.AnalyticsTrackingImpl
import br.com.atitude.finder.data.local.EncryptedSharedPreferencesManager
import br.com.atitude.finder.data.network.NetworkApi
import br.com.atitude.finder.data.network.RetrofitConfig
import br.com.atitude.finder.data.remoteconfig.AppRemoteConfig
import br.com.atitude.finder.repository.ApiRepository
import br.com.atitude.finder.repository.ApiRepositoryImpl
import br.com.atitude.finder.repository.SharedPrefs
import br.com.atitude.finder.repository.SharedPrefsImpl
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.Locale

val dataModules = module {
    single {
        Geocoder(androidContext(), Locale("pt_br", "Brazil"))
    }

    single { AppRemoteConfig() }

    single { EncryptedSharedPreferencesManager.create(androidContext()) }

    single {
        RetrofitConfig.instantiate(get()).create(NetworkApi::class.java)
    }

    factory<AnalyticsTracking> { AnalyticsTrackingImpl(Firebase.analytics) }
    factory<ApiRepository> { ApiRepositoryImpl(get()) }
    factory<SharedPrefs> { SharedPrefsImpl(get()) }
}