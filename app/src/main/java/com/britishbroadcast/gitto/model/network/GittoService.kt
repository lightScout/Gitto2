package com.britishbroadcast.gitto.model.network

import com.britishbroadcast.gitto.model.data.GitUsersResponse
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH_MULTIPLE_USERS
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH_SINGLE_USER
import com.britishbroadcast.gitto.util.Constants.Companion.Q
import com.britishbroadcast.gitto.util.Constants.Companion.USER_NAME
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GittoService {

    @GET(API_PATH_SINGLE_USER)
    fun getGitUser(@Path(USER_NAME) userName: String): Single<String>

    @GET(API_PATH_MULTIPLE_USERS)
    fun searchUserByName(@Query(Q) userName: String ): Single<GitUsersResponse>

}