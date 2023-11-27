package br.com.atitude.finder.presentation.map

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import br.com.atitude.finder.presentation._base.EXTRA_ADDRESS_LATITUDE
import br.com.atitude.finder.presentation._base.EXTRA_ADDRESS_LONGITUDE
import br.com.atitude.finder.presentation._base.intentPointMap
import com.google.android.gms.maps.model.LatLng

class PointMapResultContract : ActivityResultContract<String?, LatLng?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return context.intentPointMap(input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): LatLng? {
        if (intent == null) return null

        if (resultCode == RESULT_OK) {
            val addressLatitude = intent.getDoubleExtra(EXTRA_ADDRESS_LATITUDE, 0.0)
            val addressLongitude = intent.getDoubleExtra(EXTRA_ADDRESS_LONGITUDE, 0.0)
            return LatLng(addressLatitude, addressLongitude)
        }

        return null
    }
}