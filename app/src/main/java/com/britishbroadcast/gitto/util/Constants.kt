package com.britishbroadcast.gitto.util

import java.lang.annotation.RetentionPolicy

class Constants {

    companion object{
        const val BASE_URL = "https://api.github.com"
        const val API_PATH_SINGLE_USER = "users/{user_name}/repos"
        const val API_PATH_MULTIPLE_USERS = "search/users"
        const val API_PATH_COMMITS = "/repos/{user_name}/{repo_name}/commits"
        const val REPO_NAME = "repo_name"
        const val Q = "q"
        const val USER_NAME = "user_name"
        const val GIT_CLIENT_ID = "d5fe5b93620217eaa352"
        const val GIT_CLIENT_SECRET = "2dc54f244c896286b9e2680b496ae50111146166"
        const val GIT_REQUEST_URL = "https://github.com/login/oauth/authorize"
        const val GIT_REDIRECT_URI = "gitto://callback"
    }
}