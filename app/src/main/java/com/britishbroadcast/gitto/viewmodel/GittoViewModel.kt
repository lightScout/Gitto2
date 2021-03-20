package com.britishbroadcast.gitto.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.repository.GittoRepository

class GittoViewModel: ViewModel() {

    private val gittoRepository = GittoRepository()

    var gittoLiveData = MutableLiveData<GitResponse>()

    fun getGitUser(userName: String){
        gittoLiveData.postValue(gittoRepository.getUserName(userName))
    }



}