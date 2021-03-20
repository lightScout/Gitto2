package com.britishbroadcast.gitto.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.network.GittoRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GittoRepository {

    private val gittoRetrofit = GittoRetrofit()

    private val compositeDisposable = CompositeDisposable()

    fun getUserName(userName: String): GitResponse{
        var gitResponse = GitResponse()
        compositeDisposable.add(
            gittoRetrofit.getGitUser(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    gitResponse = it
                }, {
                    Log.d("TAG_J", "getUserName: ")
                })
        )
        return gitResponse
    }

}