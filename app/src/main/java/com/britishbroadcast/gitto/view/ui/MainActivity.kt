package com.britishbroadcast.gitto.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.model.network.GittoRetrofit
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class MainActivity : AppCompatActivity() {
    private val gittoViewModel = GittoViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gittoViewModel.getGitUser("lightscout")

    }
}