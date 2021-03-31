package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.AddFragmentLayoutBinding
import com.britishbroadcast.gitto.view.adapter.SearchUserItemAdapter
import com.britishbroadcast.gitto.view.adapter.UserItemAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import java.util.zip.Inflater

class AddFragment: Fragment(), SearchUserItemAdapter.SearchUserItemDelegate {
    interface AddFragmentDelegate{
        fun addUser()
    }

    private val gittoViewModel by activityViewModels<GittoViewModel>()
    private val searchUserItemAdapter = SearchUserItemAdapter(mutableListOf(), this)
    private lateinit var binding: AddFragmentLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mainRecyclerView.adapter = searchUserItemAdapter

        binding.searchButton.setOnClickListener {
            if(validSearch()){
                gittoViewModel.searchUserByName(binding.searchEditText.text.toString().trim())
            }
        }
        gittoViewModel.getRepository().gitSearchResponseLiveData.observe(viewLifecycleOwner, Observer {
            searchUserItemAdapter.updateOwners(it)

        })


    }

    private fun validSearch(): Boolean {
        return binding.searchEditText.text.isNotEmpty()
    }

    override fun addUser(userName: String) {
      gittoViewModel.getRepository().getUserName(userName)
    }
}