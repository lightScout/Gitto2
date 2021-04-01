package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.SettingsFragmentLayoutBinding
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import kotlin.math.log

class SettingsFragment(): Fragment() {
    private val gittoViewModel by activityViewModels<GittoViewModel>()
    private lateinit var settingsDelegate: SettingsDelegate


    private var baseBinding: SettingsFragmentLayoutBinding? = null
    private val referenceBinding: SettingsFragmentLayoutBinding get() = baseBinding ?: throw IllegalStateException("Trying to access the binding outside the view lifecycle.")

    interface SettingsDelegate{
        fun logout()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SettingsFragmentLayoutBinding.inflate(inflater,container, false).also {
        baseBinding = it
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        referenceBinding.logoutButton.setOnClickListener {
            settingsDelegate.logout()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingsDelegate = (context as MainActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseBinding = null
    }

}