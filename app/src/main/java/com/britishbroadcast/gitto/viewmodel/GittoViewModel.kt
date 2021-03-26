
package com.britishbroadcast.gitto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.RoomDatabase
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.repository.GittoRepository
import com.google.gson.Gson

class GittoViewModel(application: Application): AndroidViewModel(application) {

    private val gittoRepository = GittoRepository(application)



    fun getGitUser(userName: String){
        gittoRepository.getUserName(userName)
    }

    fun searchUserByName(userName: String){
        gittoRepository.searchUserByName(userName)
    }

    fun getRepository(): GittoRepository{
        return gittoRepository
    }

    fun getAllDataFromDB(): List<GitResponse>{
        var gitResponseList = mutableListOf<GitResponse>()

        gittoRepository.getDataBase().gittoDAO().getAllItems().forEach {
            val gitResponse = Gson().fromJson(it.userData, GitResponse::class.java)
            gitResponseList.add(gitResponse)
        }
        return gitResponseList
    }


    fun populateDB(){
            getGitUser("lightScout")
            getGitUser("sifatsaif95")
            getGitUser("MindaRah")
    }

//    fun populateHomeRecyclerView() {
//
//            GlobalScope.launch{
//                val gittoDataList = gittoDataBase.gittoDAO().getAllItems()
//
//
//
//        }



//            var gitResponseList = mutableListOf<GitResponse>()
//            gittoDataList.forEach {
//                // From gittoData to GitReponse
//                val gitResponse = Gson().fromJson(it.userData, GitResponse::class.java)
//                gitResponseList.add(gitResponse)
//            }
//            gitResponseLiveData.postValue(gitResponseList)

    }

//    fun getAccessToken(code: String){
//        gittoRepository.getAccessToken(GIT_CLIENT_ID, GIT_CLIENT_SECRET, code)
//    }

