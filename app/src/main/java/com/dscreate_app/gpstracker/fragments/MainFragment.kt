package com.dscreate_app.gpstracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dscreate_app.gpstracker.databinding.FragmentMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

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
        initOSM()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingsOsm() {
        Configuration.getInstance().load(
            requireActivity(),
            activity?.getSharedPreferences(SHARED_PREF_TABLE_NAME, Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    private fun initOSM() = with(binding) {
       map.controller.setZoom(20.0)
        val mLocProvider = GpsMyLocationProvider(activity)
        val myLocOverlay = MyLocationNewOverlay(mLocProvider, map)
        myLocOverlay.enableMyLocation()
        myLocOverlay.enableFollowLocation()
        myLocOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(myLocOverlay)
        }
    }

    companion object {

        private const val SHARED_PREF_TABLE_NAME = "osm_pref"

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}