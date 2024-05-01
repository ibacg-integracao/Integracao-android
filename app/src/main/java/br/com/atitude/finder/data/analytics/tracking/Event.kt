package br.com.atitude.finder.data.analytics.tracking

import androidx.core.os.bundleOf
import br.com.atitude.finder.BuildConfig

abstract class Event(val name: String) {
    open fun toBundle() = bundleOf()
}