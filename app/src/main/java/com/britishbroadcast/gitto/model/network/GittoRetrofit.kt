package com.britishbroadcast.gitto.model.network

import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.util.Constants.Companion.BASE_URL
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class GittoRetrofit {

    private val gittoService: GittoService

    init {
        gittoService = createGittoService(createRetrofit())
    }

    private fun createRetrofit(): Retrofit{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createGittoService(retrofit: Retrofit): GittoService = retrofit.create(GittoService::class.java)

    fun getGitUser(userName: String): Single<GitResponse> = gittoService.getGitUser(userName)

}