package com.britishbroadcast.gitto.view.ui

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.HomeFragmentLayoutBinding
import com.britishbroadcast.gitto.databinding.UserItemLayoutBinding
import com.britishbroadcast.gitto.model.data.Owner
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class HomeFragment: Fragment() {
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    private val userItemAdapter = UserItemAdapter(mutableListOf())
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

//        binding.apply {
//            userRecyclerview.adapter = userItemAdapter
//        }
//
//        gittoViewModel.getGitUser("lightscout")
//        val listOwner: MutableList<Owner> = mutableListOf()
//        gittoViewModel.gittoLiveData.observe(viewLifecycleOwner, Observer {
//            if(it[0]!=null) {
//                listOwner.add(it[0].owner)
//                Log.d("TAG_X", it[0].owner.login)
//                userItemAdapter.updateOwners(listOwner)
//            }
//        })





    }

}