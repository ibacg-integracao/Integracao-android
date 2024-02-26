package br.com.atitude.finder.data.analytics.tracking

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.ParametersBuilder

abstract class Event(val name: String) {
    abstract fun toBundle(): Bundle
}