package com.britishbroadcast.gitto.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class MainActivity : AppCompatActivity() {
    private val gittoViewModel = GittoViewModel()
    private val splashScreenFragment = SplashScreenFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gittoViewModel.getGitUser("lightscout")

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                ).replace(R.id.main_frameLayout, splashScreenFragment)
                .addToBackStack(null)
                .commit()

    }
}