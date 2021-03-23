package com.britishbroadcast.gitto.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.network.GittoRetrofit
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GittoRepository {

    private val gittoRetrofit = GittoRetrofit()

    private val compositeDisposable = CompositeDisposable()
    var gittoLiveData = MutableLiveData<String>()

    fun getUserName(userName: String){
        compositeDisposable.add(
                gittoRetrofit.getGitUser(userName)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            gittoLiveData.postValue(it)

                        }, {
                            Log.d("TAG_J", it.localizedMessage)
                        })
        )

    }

}