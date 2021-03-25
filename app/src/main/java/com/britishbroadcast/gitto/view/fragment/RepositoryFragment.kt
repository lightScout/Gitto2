package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.databinding.RepositoriesFragmentLayoutBinding
import com.britishbroadcast.gitto.view.adapter.RepositoriesItemAdapter
import com.britishbroadcast.gitto.view.ui.MainActivity

class RepositoriesFragment: Fragment(), RepositoriesItemAdapter.RepositoryItemDelegate {

    interface  RepositoryInterface{

    }

    private lateinit var binding: RepositoriesFragmentLayoutBinding
    private val repositoriesItemAdapter = RepositoriesItemAdapter(mutableListOf(), this)
    private lateinit var repositoryInterface: RepositoryInterface

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = RepositoriesFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //repositoryInterface = (context as HomeFragment)
    }

    override fun showCommits() {

    }

}