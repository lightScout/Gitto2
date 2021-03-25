package com.britishbroadcast.gitto.model.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.britishbroadcast.gitto.model.DataBase.GittoDataBase
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.GittoData
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

    var gitResponseList = mutableListOf<GitResponse>()

    fun getUserName(userName: String){

        compositeDisposable.add(
                gittoRetrofit.getGitUser(userName)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            // Adding to the DB
                            var gittoData = GittoData(it)
                            gittoDataBase.gittoDAO().insertGittoItem(gittoData)

                            val gitResponse = Gson().fromJson(gittoData.userData, GitResponse::class.java)
                            gitResponseList.add(gitResponse)
                            gitResponseLiveData.postValue(gitResponseList)

                        }, {
                            Log.d("TAG_J", it.localizedMessage)
                        })
        )

    }

}