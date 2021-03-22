package com.britishbroadcast.gitto.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.britishbroadcast.gitto.model.DataBase.GittoDataBase
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.GittoData
import com.britishbroadcast.gitto.model.repository.GittoRepository
import com.google.gson.Gson

class GittoViewModel(application: Application): AndroidViewModel(application) {

    private val gittoRepository = GittoRepository()

    private val gittoDataBase = Room.databaseBuilder(application.applicationContext,
        GittoDataBase::class.java,
            "gitto.db"
            ).build()


    fun getGitUser(userName: String){
        gittoRepository.getUserName(userName)

    }

    fun getRepository(): GittoRepository{
        return gittoRepository
    }

    fun insertItemToDB(gitResponse: String){

        // Gitto object from String(json) Api call
        val gittoData = GittoData(gitResponse)

        // Adding gittoData to DB
        // gittoDataBase.gittoDAO().insertGittoItem(gittoData)


        // From gittoData to GitReponse
        // val gitResponse = Gson().fromJson(gittoData.userData, GitResponse::class.java)

    }




}