package br.com.atitude.finder.presentation.map

import android.content.Intent
import android.os.Bundle
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityPointMapBinding
import br.com.atitude.finder.extensions.visibleOrGone
import br.com.atitude.finder.presentation._base.BaseActivity
import br.com.atitude.finder.presentation._base.EXTRA_ADDRESS_LATITUDE
import br.com.atitude.finder.presentation._base.EXTRA_ADDRESS_LINE
import br.com.atitude.finder.presentation._base.EXTRA_ADDRESS_LONGITUDE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class PointMapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityPointMapBinding
    private val pointMapViewModel: PointMapViewModel by viewModel()

    private var marker: Marker? = null

    override fun getViewModel() = pointMapViewModel

    private fun getInitialAddress(): String? {
        return intent.getStringExtra(EXTRA_ADDRESS_LINE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun configMap() {
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.isBuildingsEnabled = false
    }

    private fun searchAddressOnMap() {
        val address = binding.textInputAddress.text?.toString().orEmpty()
        pointMapViewModel.searchAddress(address)
    }

    private fun initViews() {
        binding.textInputLayoutAddress.setEndIconOnClickListener {
            searchAddressOnMap()
        }

        binding.buttonConfirm.setOnClickListener {
            val address = mMap.cameraPosition.target
            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_LATITUDE, address.latitude)
                putExtra(EXTRA_ADDRESS_LONGITUDE, address.longitude)
            })
            finish()
        }
    }

    private fun setMarker(coordinates: LatLng) {

        if (marker == null) {
            marker = mMap.addMarker(MarkerOptions().position(coordinates))
        }
    }

    private fun initObservers() {
        with(pointMapViewModel) {
            searching.observe(this@PointMapActivity) { searching ->
                binding.textInputLayoutAddress.setEndIconActivated(!searching)
            }
            cameraTarget.observe(this@PointMapActivity) { target ->
                if (target != null) {
                    val cameraPosition = CameraPosition.builder()
                        .target(target)
                        .zoom(17.5F)
                        .build()

                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    setMarker(target)
                }
            }

            lastSearchedAddress.observe(this@PointMapActivity) { address ->
                binding.textViewSelectedAddress.visibleOrGone(false)

                if (address != null) {
                    binding.textViewSelectedAddress.visibleOrGone(true)
                    binding.textViewSelectedAddress.text = address.getAddressLine(0)
                }
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        configMap()
        initViews()

        val initialAddress = getInitialAddress()

        if(initialAddress != null) {
           binding.textInputAddress.text?.clear()
           binding.textInputAddress.text?.insert(0, initialAddress)
           searchAddressOnMap()
        }
    }
}