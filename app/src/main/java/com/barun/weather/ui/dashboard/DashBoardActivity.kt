package com.barun.weather.ui.dashboard

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.barun.weather.R
import com.barun.weather.databinding.ActivityDashboardBinding
import com.barun.weather.databinding.ToolbarMainBinding
import com.barun.weather.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class DashBoardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var toolBarBinding: ToolbarMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        toolBarBinding = binding.toolbar
        setContentView(binding.root)
        setSupportActionBar(toolBarBinding.toolbarMain)

    }

    override fun onResume() {
        super.onResume()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }
}