package com.britishbroadcast.gitto.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.britishbroadcast.gitto.model.DataBase.GittoDataBase
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.GittoData
import com.britishbroadcast.gitto.model.data.Owner
import com.britishbroadcast.gitto.model.repository.GittoRepository
import com.google.gson.Gson

class GittoViewModel(application: Application): AndroidViewModel(application) {

    private val gittoRepository = GittoRepository()

    private val gittoDataBase = Room.databaseBuilder(application.applicationContext,
        GittoDataBase::class.java,
            "gitto.db"
            ).build()

    val gitResponseLiveData = MutableLiveData<List<GitResponse>>()


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
        Thread{
            gittoDataBase.gittoDAO().insertGittoItem(gittoData)
        }.start()

    }

    fun readDataFromDB(){

        var gittoDataList = listOf<GittoData>()
        Thread {
            gittoDataList = gittoDataBase.gittoDAO().getAllItems()
        }.start()

        if(gittoDataList.isNotEmpty()) {
            var gitResponseList = mutableListOf<GitResponse>()
            gittoDataList.forEach {
                // From gittoData to GitReponse
                val gitResponse = Gson().fromJson(it.userData, GitResponse::class.java)
                gitResponseList.add(gitResponse)
            }
            gitResponseLiveData.postValue(gitResponseList)
        }else{
            //Adding users from API to RoomDatabase
            getGitUser("lightScout")
            getGitUser("sifatsaif95")
            getGitUser("MindaRah")
        }
    }
}