package com.dscreate_app.gpstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dscreate_app.gpstracker.databinding.FragmentViewTrackBinding

class ViewTrackFragment : Fragment() {

    private var _binding: FragmentViewTrackBinding? = null
    private val binding: FragmentViewTrackBinding
        get() = _binding ?: throw RuntimeException("FragmentViewTrackBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}