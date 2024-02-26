package br.com.atitude.finder

import android.app.Application
import androidx.core.os.bundleOf
import br.com.atitude.finder.di.appModules
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationInitialization: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationInitialization)
            androidLogger(Level.DEBUG)
            modules(appModules)
        }
    }
}