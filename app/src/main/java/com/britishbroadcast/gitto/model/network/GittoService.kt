package com.britishbroadcast.gitto.model.network

import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH
import com.britishbroadcast.gitto.util.Constants.Companion.USER_NAME
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface GittoService {

    @GET(API_PATH)
    fun getGitUser(@Path(USER_NAME) userName: String): Single<String>

}