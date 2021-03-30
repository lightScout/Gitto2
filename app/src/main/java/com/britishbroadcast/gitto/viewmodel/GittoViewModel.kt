package com.britishbroadcast.gitto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.RoomDatabase
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.repository.GittoRepository
import com.britishbroadcast.gitto.util.Constants.Companion.JUAN
import com.britishbroadcast.gitto.util.Constants.Companion.MINDA
import com.britishbroadcast.gitto.util.Constants.Companion.SAIF
import com.google.gson.Gson

class GittoViewModel(application: Application) : AndroidViewModel(application) {

    private val gittoRepository = GittoRepository(application)


    fun getGitUser(userName: String) {
        gittoRepository.getUserName(userName)
    }

    fun searchUserByName(userName: String) {
        gittoRepository.searchUserByName(userName)
    }

    fun getRepository(): GittoRepository {
        return gittoRepository
    }

    fun getAllDataFromDB(): List<GitResponse> {
        var gitResponseList = mutableListOf<GitResponse>()

        gittoRepository.getDataBase().gittoDAO().getAllItems().forEach {
            val gitResponse = Gson().fromJson(it.userData, GitResponse::class.java)
            gitResponseList.add(gitResponse)
        }
        return gitResponseList
    }


    fun populateDB(userName: String, oAuth: String) {
        // Check wich user has just sign with gitHub
        // If no user has sign-in with gitHub the app will load the three base user
        // without any private repos
        if (oAuth == "") {
            getGitUser(JUAN)
            getGitUser(SAIF)
            getGitUser(MINDA)
        } else {
            if (userName != JUAN && userName != SAIF && userName != MINDA) {
                getGitUser(JUAN)
                getGitUser(SAIF)
                getGitUser(MINDA)
                gittoRepository.getGitUserPrivateRepo(oAuth)
            } else {

                when (userName) {
                    JUAN -> {
                        getGitUser(SAIF)
                        getGitUser(MINDA)
                        gittoRepository.getGitUserPrivateRepo(oAuth)
                    }
                    MINDA -> {
                        getGitUser(JUAN)
                        getGitUser(SAIF)
                        gittoRepository.getGitUserPrivateRepo(oAuth)
                    }
                    SAIF -> {
                        getGitUser(MINDA)
                        getGitUser(JUAN)
                        gittoRepository.getGitUserPrivateRepo(oAuth)
                    }
                }

            }
        }

        gittoRepository.cleanCompositeDisposable()


    }


}


