package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britishbroadcast.gitto.databinding.HomeFragmentLayoutBinding
import com.britishbroadcast.gitto.model.data.Owner
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class HomeFragment: Fragment(), UserItemAdapter.UserItemDelegate {
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    private val userItemAdapter = UserItemAdapter(mutableListOf(), this)
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


        binding.apply {
            userRecyclerview.adapter = userItemAdapter
        }
        userItemAdapter.updateOwners(getUsers())
    }

    override fun showRepositories() {

    }

    fun getUsers(): List<Owner>{
        var ownerList = mutableListOf<Owner>()

        gittoViewModel.gitResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.forEach { gitResponse ->
                ownerList.add(gitResponse[0].owner)
            }

        })
        return ownerList
    }


}