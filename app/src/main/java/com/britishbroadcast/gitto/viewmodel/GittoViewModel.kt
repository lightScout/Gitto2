package com.britishbroadcast.gitto.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.repository.GittoRepository

class GittoViewModel: ViewModel() {

    private val gittoRepository = GittoRepository()

    fun getGitUser(userName: String){
        gittoRepository.getUserName(userName)
    }

    fun getRepository(): GittoRepository{
        return gittoRepository
    }



}