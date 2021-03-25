
package com.britishbroadcast.gitto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.britishbroadcast.gitto.model.repository.GittoRepository

class GittoViewModel(application: Application): AndroidViewModel(application) {

    private val gittoRepository = GittoRepository(application)



    fun getGitUser(userName: String){
        gittoRepository.getUserName(userName)
    }

    fun getRepository(): GittoRepository{
        return gittoRepository
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

