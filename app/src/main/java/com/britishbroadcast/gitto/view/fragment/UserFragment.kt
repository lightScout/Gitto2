package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britishbroadcast.gitto.databinding.UserFragmentLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import java.lang.IllegalStateException

class UserFragment(val userFragmentDelete: HomeFragment) : Fragment(),
    UserItemAdapter.UserItemDelegate {

    interface UserFragmentInterface {
        fun displayRepositoriesFragment(login: String)
    }


    private var gitReposeList = listOf<GitResponse>()
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = UserFragmentLayoutBinding.inflate(inflater, container, false).also {
    }.root


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG_J", "onDestroyView: UserFragment")
        gittoViewModel.getRepository().gitRepositoryLiveData.removeObservers(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = UserFragmentLayoutBinding.bind(view)
        val userItemAdapter = UserItemAdapter(mutableListOf(), this)
        gittoViewModel.getRepository().gitResponseLiveData.observe(viewLifecycleOwner) { data ->
            with(binding) {
                userRecyclerview.adapter = userItemAdapter
                gitReposeList = data
                userItemAdapter.updateOwners(data)

            }

        }

    }

    override fun showRepositories(login: String) {
        userFragmentDelete.displayRepositoriesFragment(login)
        gittoViewModel.getRepository().gitRepositoryLiveData.postValue(gitReposeList)
    }


}