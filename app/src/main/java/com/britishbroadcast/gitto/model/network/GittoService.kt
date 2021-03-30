package com.britishbroadcast.gitto.model.network

import com.britishbroadcast.gitto.model.data.AccessToken
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.GitUserCommit
import com.britishbroadcast.gitto.model.data.GitUsersResponse
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH_COMMITS
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH_MULTIPLE_USERS
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH_PRIVATE_REPO
import com.britishbroadcast.gitto.util.Constants.Companion.API_PATH_SINGLE_USER
import com.britishbroadcast.gitto.util.Constants.Companion.Q
import com.britishbroadcast.gitto.util.Constants.Companion.REPO_NAME
import com.britishbroadcast.gitto.util.Constants.Companion.USER_NAME
import io.reactivex.Single
import retrofit2.http.*

interface GittoService {

    @GET(API_PATH_SINGLE_USER)
    fun getGitUser(@Path(USER_NAME) userName: String): Single<String>

    @GET(API_PATH_MULTIPLE_USERS)
    fun searchUserByName(@Query(Q) userName: String ): Single<GitUsersResponse>

    @GET(API_PATH_COMMITS)
    fun getGitUserRepositoryCommits(@Path(USER_NAME) userName: String, @Path(REPO_NAME) repoName: String): Single<GitUserCommit>

    @GET(API_PATH_PRIVATE_REPO)
    fun getGitUserPrivateRepo(@Header("Authorization") authorization: String): Single<GitResponse>


    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientID: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String): Single<AccessToken>
}