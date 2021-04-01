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

    private var baseBinding: CommitsFragmentLayoutBinding? = null
    private val referenceBinding: CommitsFragmentLayoutBinding get() = baseBinding ?: throw IllegalStateException("Trying to access the binding outside of the view lifecycle.")
    private val commitItemAdapter = CommitItemAdapter(mutableListOf())
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  = CommitsFragmentLayoutBinding.inflate(inflater, container, false).also {
        baseBinding = it
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        referenceBinding.apply {
            commitsRecyclerview.adapter = commitItemAdapter
        }

        gittoViewModel.getRepository().gitCommitsLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            commitItemAdapter.updateCommits(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        gittoViewModel.getRepository().gitCommitsLiveData.removeObservers(this)
        referenceBinding.commitsRecyclerview.layoutManager = null
        referenceBinding.commitsRecyclerview.adapter = null
        baseBinding = null
    }

}