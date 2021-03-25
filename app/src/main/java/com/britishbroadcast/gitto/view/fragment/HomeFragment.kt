package com.britishbroadcast.gitto.view.fragment

import android.content.Context
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
import com.britishbroadcast.gitto.model.data.Owner
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class HomeFragment: Fragment(), UserItemAdapter.UserItemDelegate, RepositoriesFragment.RepositoryInterface {
    interface HomeFragmentInterface {
        fun displayRepositoriesFragment()
    }

    private val gittoViewModel by activityViewModels<GittoViewModel>()

    private val userItemAdapter = UserItemAdapter(mutableListOf(), this)
    private lateinit var binding: HomeFragmentLayoutBinding
    private lateinit var homeFragmentInterface: HomeFragmentInterface
    private val repositoriesFragment = RepositoriesFragment()

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

        gittoViewModel.getRepository().gitResponseLiveData.observe(viewLifecycleOwner, Observer {
            userItemAdapter.updateOwners(it)
        })


    }

    override fun showRepositories() {
        binding.homeFrameLayout.visibility = View.VISIBLE
        parentFragmentManager.popBackStack()
        homeFragmentInterface.displayRepositoriesFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeFragmentInterface = (context as MainActivity)
    }

}
