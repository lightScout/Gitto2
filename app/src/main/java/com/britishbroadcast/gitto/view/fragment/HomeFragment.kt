package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.HomeFragmentLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import kotlinx.android.synthetic.*

class HomeFragment: Fragment(), UserFragment.UserFragmentInterface, RepositoriesFragment.RepositoryInterface{

    private lateinit var binding: HomeFragmentLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userFragment = UserFragment(this)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                ).add(binding.homeFrameLayout.id, userFragment)
                .addToBackStack(userFragment.tag)
                .commit()

    }

    override fun displayRepositoriesFragment(login: String) {
        val newRepositoryFragment = RepositoriesFragment(this)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(binding.homeFrameLayout.id, newRepositoryFragment.also{
                val bundle = Bundle()
                bundle.putString("USER", login)
                it.arguments = bundle
            })
            .addToBackStack(newRepositoryFragment.tag)
            .commit()
    }

    override fun displayCommitsFragment() {
        val newCommitsFragment = CommitsFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(binding.homeFrameLayout.id, newCommitsFragment)
            .addToBackStack(null)
            .commit()
    }


}