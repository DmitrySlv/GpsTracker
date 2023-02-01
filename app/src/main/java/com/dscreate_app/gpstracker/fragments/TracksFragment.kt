package com.dscreate_app.gpstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscreate_app.gpstracker.MainApp
import com.dscreate_app.gpstracker.adapters.TrackAdapter
import com.dscreate_app.gpstracker.databinding.FragmentTracksBinding
import com.dscreate_app.gpstracker.viewModels.MainViewModel
import com.dscreate_app.gpstracker.viewModels.ViewModelFactory

class TracksFragment : Fragment() {

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
        adapter = TrackAdapter()
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

    companion object {

        @JvmStatic
        fun newInstance() = TracksFragment()
    }
}