package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britishbroadcast.gitto.databinding.UserFragmentLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import java.lang.IllegalStateException

class UserFragment(val userFragmentDelete: HomeFragment): Fragment(), UserItemAdapter.UserItemDelegate {

    interface UserFragmentInterface {
        fun displayRepositoriesFragment(login: String)
    }

    // Reference/Ghost patter
    // Where base var can be null
    // And reference var can't
    private var baseBinding: UserFragmentLayoutBinding? = null
    private val referenceBinding: UserFragmentLayoutBinding get() = baseBinding ?: throw IllegalStateException("Trying to access the binding outside of the view lifecycle.")


    private val userItemAdapter = UserItemAdapter(mutableListOf(), this)
    private var gitReposeList = listOf<GitResponse>()
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = UserFragmentLayoutBinding.inflate(inflater, container, false).also {
                baseBinding = it
            }.root



    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG_J", "onDestroyView: UserFragment")
        referenceBinding.userRecyclerview.layoutManager = null
        referenceBinding.userRecyclerview.adapter = null
        gittoViewModel.getRepository().gitRepositoryLiveData.removeObservers(this)
        baseBinding = null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        referenceBinding.apply {
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


}