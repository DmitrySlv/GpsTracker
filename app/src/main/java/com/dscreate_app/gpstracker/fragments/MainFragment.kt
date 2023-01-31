package com.dscreate_app.gpstracker.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dscreate_app.gpstracker.R
import com.dscreate_app.gpstracker.database.TrackItem
import com.dscreate_app.gpstracker.databinding.FragmentMainBinding
import com.dscreate_app.gpstracker.location.LocationModel
import com.dscreate_app.gpstracker.location.LocationService
import com.dscreate_app.gpstracker.utils.DialogManager
import com.dscreate_app.gpstracker.utils.TimeUtils
import com.dscreate_app.gpstracker.utils.checkPermission
import com.dscreate_app.gpstracker.utils.showToast
import com.dscreate_app.gpstracker.viewModels.MainViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    private lateinit var permLauncher: ActivityResultLauncher<Array<String>>
    private var isServiceRunning = false
    private var timer: Timer? = null
    private var startTime = 0L
    private var polyLine: Polyline? = null
    private var firstStart: Boolean = true
    private var locationModel: LocationModel? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerPermissions()
        setOnClicks()
        checkServiceState()
        updateTime()
        registerLocReceiver()
        locationUpdates()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(requireActivity())
            .unregisterReceiver(receiver)
    }

    private fun locationUpdates() = with(binding) {
        viewModel.locationUpdates.observe(viewLifecycleOwner) {
            val distance =
                getString(R.string.distance_tv) + String.format("%.1f", it.distance) +
                        getString(R.string.meter_tv)
            val speed =
                getString(R.string.speed_tv) + String.format("%.1f", it.speed) +
                        getString(R.string.speed_tv_in_meter)
            val averageSpeed =
                getString(R.string.speed_average_tv) + String.format(getAverageSpeed(it.distance)) +
                        getString(R.string.speed_tv_in_meter)

            tvDistance.text = distance
            tvSpeed.text = speed
            tvAverageSpeed.text = averageSpeed
            locationModel = it
            updatePolyLine(it.geoPointsList)
        }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = Timer()
        startTime = LocationService.startTime
        timer?.schedule(object : TimerTask() {
            override fun run() {
              activity?.runOnUiThread {
                 viewModel.timeData.value = getCurrentTime()
              }
            }
        }, 1000, 1000)
    }

    private fun getAverageSpeed(distance: Float): String {
        return String.format(
            "%.1f",
            (distance / ((System.currentTimeMillis() - startTime) / 1000.0f))
        )
    }

    private fun getCurrentTime(): String {
        return getString(R.string.time_tv) + TimeUtils.getTime(
            System.currentTimeMillis() - startTime
        )
    }

    private fun geoPointsToString(list: ArrayList<GeoPoint>): String {
        val sBuilder = StringBuilder()
        list.forEach {
            sBuilder.append("${it.latitude}, ${it.longitude}/")
        }
        Log.d("MyLog", "Points: $sBuilder")
        return sBuilder.toString()
    }

    private fun updateTime() {
      viewModel.timeData.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }
    }

    private fun setOnClicks() = with(binding){
        val listener = onClicks()
        fStartStop.setOnClickListener(listener)
    }

    private fun onClicks(): OnClickListener {
        return OnClickListener {
            when(it.id) {
                R.id.fStartStop -> {
                    startStopService()
                }
            }
        }
    }

    private fun checkServiceState() {
        isServiceRunning = LocationService.isRunning
        if (isServiceRunning) {
            binding.fStartStop.setImageResource(R.drawable.ic_stop)
            startTimer()
        }
    }

    private fun startStopService() {
        if (!isServiceRunning) {
            startLocService()
        } else {
            activity?.stopService(Intent(activity, LocationService::class.java))
            binding.fStartStop.setImageResource(R.drawable.ic_play)
            timer?.cancel()
            DialogManager.showSaveDialog(
                requireContext(),
                getTrackItem(),
                object : DialogManager.Listener {
                override fun onClick() {
                    showToast("Маршрут сохранён!")
                }
            })
        }
        isServiceRunning = !isServiceRunning
    }

    private fun getTrackItem() = TrackItem (
            null,
            getCurrentTime(),
            TimeUtils.getDate(),
            String.format("%.1f", locationModel?.distance?.div(1000) ?: 0),
            getAverageSpeed(locationModel?.distance ?: 0.0f),
            geoPointsToString(locationModel?.geoPointsList ?: arrayListOf())
    )

    private fun startLocService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(Intent(activity, LocationService::class.java))
        } else {
            activity?.startService(Intent(activity, LocationService::class.java))
        }
        binding.fStartStop.setImageResource(R.drawable.ic_stop)
        LocationService.startTime = System.currentTimeMillis()
        startTimer()
    }

    private fun settingsOsm() {
        Configuration.getInstance().load(
            requireActivity(),
            activity?.getSharedPreferences(SHARED_PREF_TABLE_NAME, Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    private fun initOSM() = with(binding) {
        polyLine = Polyline()
        polyLine?.outlinePaint?.color = Color.BLUE
        map.controller.setZoom(20.0)
        val mLocProvider = GpsMyLocationProvider(activity)
        val myLocOverlay = MyLocationNewOverlay(mLocProvider, map)
        myLocOverlay.enableMyLocation()
        myLocOverlay.enableFollowLocation()
        myLocOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(myLocOverlay)
            map.overlays.add(polyLine)
        }
    }

    private fun registerPermissions() {
        permLauncher = registerForActivityResult(ActivityResultContracts
            .RequestMultiplePermissions()) {
            if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                initOSM()
                checkLocationEnabled()
            } else {
                showToast(getString(R.string.toast_need_perm))
            }
        }
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionAfter10()
        } else {
            checkPermissionBefore10()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionAfter10() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            initOSM()
            checkLocationEnabled()
        } else {
            permLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        }
    }

    private fun checkPermissionBefore10() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initOSM()
            checkLocationEnabled()
        } else {
            permLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkLocationEnabled() {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isEnabled) {
            DialogManager.showLocEnabledDialog(requireActivity(),
                object : DialogManager.Listener {
                    override fun onClick() {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            )
        } else {
            showToast("GPS включен")
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationService.LOC_MODEL_INTENT) {
                val locModel = intent.getSerializableExtra(
                    LocationService.LOC_MODEL_INTENT) as LocationModel
                viewModel.locationUpdates.value = locModel
            }
        }
    }

    private fun registerLocReceiver() {
        val locFilter = IntentFilter(LocationService.LOC_MODEL_INTENT)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(receiver, locFilter)
    }

    private fun addPoint(list: ArrayList<GeoPoint>) {
        polyLine?.addPoint(list[list.size - 1])
    }

    private fun fillPolyLine(list: ArrayList<GeoPoint>) {
        list.forEach {
            polyLine?.addPoint(it)
        }
    }

    private fun updatePolyLine(list: ArrayList<GeoPoint>) {
        if (list.size > 1 && firstStart) {
            fillPolyLine(list)
            firstStart = false
        } else {
            addPoint(list)
        }
    }

    companion object {

        private const val SHARED_PREF_TABLE_NAME = "osm_pref"

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}