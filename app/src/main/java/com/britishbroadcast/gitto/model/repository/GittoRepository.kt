package com.britishbroadcast.gitto.model.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.britishbroadcast.gitto.model.DataBase.GittoDataBase
import com.britishbroadcast.gitto.model.data.*
import com.britishbroadcast.gitto.model.network.GittoRetrofit
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.withContext

class GittoRepository(val application: Application) {

    private val gittoRetrofit = GittoRetrofit()

    private val compositeDisposable = CompositeDisposable()

    private val gittoDataBase = Room.databaseBuilder(
        application.applicationContext,
        GittoDataBase::class.java,
        "gitto.db"
    ).allowMainThreadQueries()
        .build()
    val gitResponseLiveData = MutableLiveData<List<GitResponse>>()

    val gitSearchResponseLiveData = MutableLiveData<List<Item>>()

    val gitRepositoryLiveData = MutableLiveData<List<GitResponse>>()

    val gitPrivateRepoUser = MutableLiveData<String>()

    val gitCommitsLiveData = MutableLiveData<List<GitUserCommitItem>>()

    var gitResponseList = mutableListOf<GitResponse>()


    fun getDataBase(): GittoDataBase {
        return gittoDataBase
    }

    fun getUserName(userName: String) {

        compositeDisposable.add(
            gittoRetrofit.getGitUser(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    // Adding to the DB
                    val gitResponse = Gson().fromJson(it, GitResponse::class.java)
                    // API call can be returned with an empty list
                    if(!gitResponse.isEmpty()){
                        var gittoData = GittoData(gitResponse[0].owner.login, it)
                        gittoDataBase.gittoDAO().insertGittoItem(gittoData)
                        gitResponseList.add(gitResponse)
                        gitResponseLiveData.postValue(gitResponseList)
                        Toast.makeText(application,"${gitResponse[0].owner.login} added successfully!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(application, "User not added. Possibly due to empty profile", Toast.LENGTH_SHORT).show()
                    }

//                            compositeDisposable.clear()
                }, {
                    Log.d("TAG_J", it.localizedMessage)
                })
        )

    }

    fun searchUserByName(userName: String) {
        compositeDisposable.add(
            gittoRetrofit.searchUserByName(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    it.items
                }
                .subscribe({

                    gitSearchResponseLiveData.postValue(it)
                    compositeDisposable.clear()
                }, {
                    Log.d("TAG_J", it.localizedMessage)
                })
        )
    }

    fun updateDataBase() {
        val previousData = gittoDataBase.gittoDAO().getAllItems()
        gittoDataBase.clearAllTables()
        previousData.forEach {
            getUserName(it.userName)
        }

    }

    fun clearDB(){
        gittoDataBase.clearAllTables()
        gitResponseList.clear()
    }

    fun getGitUserRepoCommits(userName: String, repoName: String) {
        compositeDisposable.add(
            gittoRetrofit.getGitUserRepositoryCommits(userName, repoName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    gitCommitsLiveData.postValue(it)
                    compositeDisposable.clear()
                }, {
                    Log.d("TAG_J", it.localizedMessage)
                })
        )
    }

    fun getGitUserPrivateRepo(authorization: String) {
        compositeDisposable.add(
            gittoRetrofit.getGitUserPrivateRepo(authorization)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    val gitResponse = Gson().fromJson(it, GitResponse::class.java)
                    var gittoData = GittoData(gitResponse[0].owner.login, it)
                    gittoDataBase.gittoDAO().insertGittoItem(gittoData)
                    gitPrivateRepoUser.postValue(gitResponse[0].owner.login)
                    gitResponseList.add(gitResponse)
                    gitResponseLiveData.postValue(gitResponseList)


//                    compositeDisposable.clear()
                }, {
                    Log.d("TAG_J_error", it.localizedMessage)
                })
        )
    }

    fun getAccessToken(clientID: String, clientSecret: String, code: String) {
        compositeDisposable.add(
            gittoRetrofit.getAccessToken(clientID, clientSecret, code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TAG_J", "token ${it.accessToken}")
                    getGitUserPrivateRepo("token ${it.accessToken}")
//                    compositeDisposable.clear()
                }, {
                    Log.d("TAG_J_ERROR", it.localizedMessage)
                })
        )
    }


}