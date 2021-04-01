package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britishbroadcast.gitto.databinding.RepositoriesFragmentLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.view.adapter.RepositoriesItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class RepositoriesFragment(val repositoryFragmentDelegate: RepositoryInterface): Fragment(), RepositoriesItemAdapter.RepositoryItemDelegate {

    interface  RepositoryInterface{
        fun displayCommitsFragment()
    }

    private var baseBinding: RepositoriesFragmentLayoutBinding? = null
    private val referenceBinding: RepositoriesFragmentLayoutBinding get() = baseBinding ?: throw  IllegalStateException("Trying to access the binding outside of the view lifecycle.")

    private val repositoriesItemAdapter = RepositoriesItemAdapter(GitResponse(), this)
    private val gittoViewModel by activityViewModels<GittoViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = RepositoriesFragmentLayoutBinding.inflate(inflater, container, false).also {
        baseBinding = it
    }.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        referenceBinding.repositoriesRecyclerview.adapter = repositoriesItemAdapter
        arguments?.let{bundle ->
            val username  = bundle.getString("USER")
            gittoViewModel.getRepository().gitRepositoryLiveData.observe(viewLifecycleOwner, Observer {
                it.forEach{ gitResp ->

                    if(gitResp[0].owner.login == username)
                        repositoriesItemAdapter.updateRepositories(gitResp)
                }
            })

        }
    }

    override fun showCommits(login: String, name: String) {
        repositoryFragmentDelegate.displayCommitsFragment()
        gittoViewModel.getRepository().getGitUserRepoCommits(login, name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gittoViewModel.getRepository().gitRepositoryLiveData.removeObservers(this)
        referenceBinding.repositoriesRecyclerview.layoutManager = null
        referenceBinding.repositoriesRecyclerview.adapter = null
        baseBinding = null
    }

}