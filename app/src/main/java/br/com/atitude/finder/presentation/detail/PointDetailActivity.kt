package br.com.atitude.finder.presentation.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.atitude.finder.R
import br.com.atitude.finder.databinding.ActivityPointDetailBinding
import br.com.atitude.finder.domain.pointdetail.PointDetail
import br.com.atitude.finder.domain.pointdetail.PointDetailContact
import br.com.atitude.finder.extensions.toDate
import br.com.atitude.finder.extensions.toHour
import br.com.atitude.finder.presentation._base.EXTRA_POINT_ID
import br.com.atitude.finder.presentation._base.ToolbarActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class PointDetailActivity : ToolbarActivity() {

    private val pointId: String? by lazy { intent.getStringExtra(EXTRA_POINT_ID) }
    private lateinit var binding: ActivityPointDetailBinding

    private val detailViewModel: DetailViewModel by viewModel()
    override fun getViewModel() = detailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configToolbar(binding.toolbar)
        setFinishOnBack()

        val pointId: String = pointId ?: return

        getViewModel().pointDetail.observe(this) { state ->
            when (state) {
                is DetailViewModel.State.Success -> handleSuccessState(state.pointDetail)
            }
        }

        getViewModel().fetchPoint(pointId)
    }

    private fun handleSuccessState(pointDetail: PointDetail) {
        configViewsWithPointDetail(pointDetail)
    }

    private fun configViewsWithPointDetail(pointDetail: PointDetail) {
        binding.tvPointName.text = pointDetail.name
        binding.tvPointTag.text = pointDetail.tag
        binding.tvPointLeaderName.text = pointDetail.leaderName
        binding.tvPointWeekDay.text = getString(pointDetail.weekDay.localization)
        binding.tvPointHourDay.text =
            getString(R.string.detail_hour_format, pointDetail.hour, pointDetail.minute)
        binding.tvPointAddress.text = pointDetail.address
        binding.tvPointPostalCode.text = pointDetail.postalCode
        configContacts(pointDetail.pointContacts)
        binding.tvPointSector.text = pointDetail.sector.name

        val tripleDate = pointDetail.updatedAt.toDate()
        val date =
            getString(R.string.date_format, tripleDate.first, tripleDate.second, tripleDate.third)

        val pairHour = pointDetail.updatedAt.toHour()
        val hour = getString(R.string.hour_format, pairHour.first, pairHour.second)

        binding.tvLastUpdate.text = getString(R.string.detail_last_update, date, hour)
    }

    private fun configContacts(pointContacts: List<PointDetailContact>) {
        binding.rvContacts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvContacts.adapter = PointDetailContactAdapter(this, createEvent()).apply {
            pointDetailContact = pointContacts
        }
    }

    private fun createEvent() = object : AdapterPointContactEvent {
        override fun onCopy(pointDetailContact: PointDetailContact) {
            val plainPhone = pointDetailContact.getPlainPhone()
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "Telefone do contato",
                    plainPhone
                )
            )
            Toast.makeText(
                this@PointDetailActivity,
                getString(R.string.detail_toast_copy_phone, pointDetailContact.name),
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onCall(pointDetailContact: PointDetailContact) {
            val plainPhone = pointDetailContact.getPlainPhone()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${plainPhone}")
            startActivity(intent)
        }

        override fun onOpenWhatsApp(pointDetailContact: PointDetailContact) {
            val url = "https://api.whatsapp.com/send?phone=${pointDetailContact.getPlainPhoneWithCountryCode()}"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        }
    }
}