package com.barun.weather.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.barun.weather.databinding.FragmentDashBoardBinding
import com.barun.weather.databinding.FragmentPosterDetailsBinding
import com.barun.weather.ui.dashboard.DashBoardViewModel
import com.barun.weather.utils.loadUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class AlbumDetailsFragment: Fragment()  {

    private var binding: FragmentPosterDetailsBinding? = null
    private val viewModel: DashBoardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPosterDetailsBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sharedData.observe(viewLifecycleOwner, {
            binding?.posterImage?.loadUrl(it.url)
            binding?.title?.text = it.title
        })
    }
}