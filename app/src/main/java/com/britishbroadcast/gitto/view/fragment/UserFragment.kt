package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.room.Delete
import com.britishbroadcast.gitto.databinding.UserFragmentLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class UserFragment(val userFragmentDelete: HomeFragment): Fragment(), UserItemAdapter.UserItemDelegate {

    interface UserFragmentInterface {
        fun displayRepositoriesFragment(login: String)
    }

    private lateinit var binding: UserFragmentLayoutBinding
    private val userItemAdapter = UserItemAdapter(mutableListOf(), this)
    private var gitReposeList = listOf<GitResponse>()
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = UserFragmentLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userRecyclerview.adapter = userItemAdapter
        }

        gittoViewModel.getRepository().gitResponseLiveData.observe(viewLifecycleOwner, Observer {
            gitReposeList = it
            userItemAdapter.updateOwners(it)
        })

    }

    override fun showRepositories(login: String) {
        userFragmentDelete.displayRepositoriesFragment(login)
        gittoViewModel.getRepository().gitRepositoryLiveData.postValue(gitReposeList)
    }

    override fun onDestroy() {
        super.onDestroy()
        gittoViewModel.getRepository().gitRepositoryLiveData.removeObservers(this)
    }

}