package com.britishbroadcast.gitto.model.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.britishbroadcast.gitto.model.DataBase.GittoDataBase
import com.britishbroadcast.gitto.model.data.*
import com.britishbroadcast.gitto.model.network.GittoRetrofit
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GittoRepository(application: Application) {

    private val gittoRetrofit = GittoRetrofit()

    private val compositeDisposable = CompositeDisposable()

    private val gittoDataBase = Room.databaseBuilder(application.applicationContext,
            GittoDataBase::class.java,
            "gitto.db"
    ).allowMainThreadQueries()
            .build()
    val gitResponseLiveData = MutableLiveData<List<GitResponse>>()

    val gitSearchResponseLiveData = MutableLiveData<List<Item>>()

    val gitRepositoryLiveData = MutableLiveData<List<GitResponse>>()

    val gitCommitsLiveData = MutableLiveData<List<GitUserCommitItem>>()

    var gitResponseList = mutableListOf<GitResponse>()


    fun getDataBase(): GittoDataBase{
        return gittoDataBase
    }

    fun getUserName(userName: String){

        compositeDisposable.add(
                gittoRetrofit.getGitUser(userName)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            // Adding to the DB
                            val gitResponse = Gson().fromJson(it, GitResponse::class.java)
                            var gittoData = GittoData(gitResponse[0].owner.login,it)
                            gittoDataBase.gittoDAO().insertGittoItem(gittoData)
                            gitResponseList.add(gitResponse)
                            gitResponseLiveData.postValue(gitResponseList)
//                            compositeDisposable.clear()
                        }, {
                            Log.d("TAG_J", it.localizedMessage)
                        })
        )

    }
    fun searchUserByName(userName: String){
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

    fun updateDataBase(){
        val previousData = gittoDataBase.gittoDAO().getAllItems()
        gittoDataBase.clearAllTables()
        previousData.forEach {
            getUserName(it.userName)
        }

    }

    fun getGitUserRepoCommits(userName: String, repoName: String){
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

}