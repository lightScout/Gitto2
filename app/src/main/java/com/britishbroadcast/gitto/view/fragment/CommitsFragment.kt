package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.britishbroadcast.gitto.databinding.CommitsFragmentLayoutBinding
import com.britishbroadcast.gitto.view.adapter.CommitItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class CommitsFragment: Fragment() {
    interface CommitFragmentInterface {
        fun displayRepositoriesFragment(login: String)
    }

    private lateinit var binding: CommitsFragmentLayoutBinding
    private val commitItemAdapter = CommitItemAdapter(mutableListOf())
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CommitsFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            commitsRecyclerview.adapter = commitItemAdapter
        }

        gittoViewModel.getRepository().gitCommitsLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            commitItemAdapter.updateCommits(it)
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        gittoViewModel.getRepository().gitCommitsLiveData.removeObservers(this)

    }

}