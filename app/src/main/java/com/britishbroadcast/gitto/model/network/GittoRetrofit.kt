package com.britishbroadcast.gitto.model.network

import android.util.Log
import com.britishbroadcast.gitto.model.data.AccessToken
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.GitUserCommit
import com.britishbroadcast.gitto.model.data.GitUsersResponse
import com.britishbroadcast.gitto.util.Constants.Companion.BASE_URL
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_TOKEN_REQUEST_URL
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Retrofit implementation for GitHub API calls to get git users.
 */

class GittoRetrofit {

    private val gittoService: GittoService
    private val gittoAccessTokenService: GittoService

    init {
        gittoService = createGittoService(createRetrofit())
       gittoAccessTokenService = createGittoService(createTokenRetrofit())
    }

    private fun createRetrofit(): Retrofit{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createTokenRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(GIT_TOKEN_REQUEST_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createGittoService(retrofit: Retrofit): GittoService = retrofit.create(GittoService::class.java)

    fun getGitUser(userName: String): Single<String> = gittoService.getGitUser(userName)

    fun searchUserByName(userName: String): Single<GitUsersResponse> = gittoService.searchUserByName(userName)


    fun getGitUserRepositoryCommits(userName: String, repositoryName: String): Single<GitUserCommit> =
        gittoService.getGitUserRepositoryCommits(userName, repositoryName)

    fun getGitUserPrivateRepo(authorization: String): Single<String> = gittoService.getGitUserPrivateRepo(authorization)

    fun getAccessToken(clientID: String, clientSecret: String, code: String): Single<AccessToken> =
        gittoAccessTokenService.getAccessToken(clientID, clientSecret, code)

}