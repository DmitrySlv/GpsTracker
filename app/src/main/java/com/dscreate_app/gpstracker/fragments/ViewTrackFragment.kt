package com.dscreate_app.gpstracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dscreate_app.gpstracker.R
import com.dscreate_app.gpstracker.database.MainApp
import com.dscreate_app.gpstracker.databinding.FragmentViewTrackBinding
import com.dscreate_app.gpstracker.viewModels.MainViewModel
import com.dscreate_app.gpstracker.viewModels.ViewModelFactory
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

class ViewTrackFragment : Fragment() {

    private var _binding: FragmentViewTrackBinding? = null
    private val binding: FragmentViewTrackBinding
        get() = _binding ?: throw RuntimeException("FragmentViewTrackBinding is null")

    private val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        _binding = FragmentViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
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

    private fun getTrack() = with(binding) {
        viewModel.currentTrack.observe(viewLifecycleOwner) {
            val date = String.format(
                requireContext().getString(R.string.date_tv) + it.date)
            val speed = String.format(
                requireContext().getString(R.string.speed_average_tv) + it.speed)
            val distance = String.format(
                requireContext().getString(R.string.distance_tv) + it.distance +
            getString(R.string.distance_in_kilometer))

            tvData.text = date
            tvTime.text = it.time
            tvAverageSpeed.text = speed
            tvDistance.text = distance
        }
    }

    companion object {
        private const val SHARED_PREF_TABLE_NAME = "osm_pref"

        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}