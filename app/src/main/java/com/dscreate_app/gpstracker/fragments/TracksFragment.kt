package com.dscreate_app.gpstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.gpstracker.database.MainApp
import com.dscreate_app.gpstracker.R
import com.dscreate_app.gpstracker.adapters.TrackAdapter
import com.dscreate_app.gpstracker.database.TrackItem
import com.dscreate_app.gpstracker.databinding.FragmentTracksBinding
import com.dscreate_app.gpstracker.utils.ClickType
import com.dscreate_app.gpstracker.utils.openFragment
import com.dscreate_app.gpstracker.utils.showToast
import com.dscreate_app.gpstracker.viewModels.MainViewModel
import com.dscreate_app.gpstracker.viewModels.ViewModelFactory

class TracksFragment : Fragment(), TrackAdapter.Listener {

    private var _binding: FragmentTracksBinding? = null
    private val binding: FragmentTracksBinding
        get() = _binding ?: throw RuntimeException("FragmentTracksBinding is null")

    private val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        getTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        adapter = TrackAdapter(this@TracksFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    private fun getTracks() {
        viewModel.tracks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmpty.visibility =  if(it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onClick(trackItem: TrackItem, type: ClickType) {
       when(type) {
           ClickType.DELETE -> {
               viewModel.deleteTrack(trackItem)
               showToast(getString(R.string.toast_track_deleted))
           }
           ClickType.OPEN -> {
               viewModel.currentTrack.value = trackItem
               openFragment(ViewTrackFragment.newInstance())
           }
       }
    }

    companion object {

        @JvmStatic
        fun newInstance() = TracksFragment()
    }
}