package com.britishbroadcast.gitto.view.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_CLIENT_ID
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REDIRECT_URI
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REQUEST_URL
import com.britishbroadcast.gitto.view.adapter.GittoViewPagerAdapter
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.view.ui.fragment.LoginScreenFragment
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, SplashScreenFragment.SplashScreenInterface, LoginScreenFragment.LoginDelegate {
    private val splashScreenFragment = SplashScreenFragment()
    private val loginScreenFragment = LoginScreenFragment()
    private lateinit var binding: ActivityMainBinding
    private lateinit var gittoViewPagerAdapter: GittoViewPagerAdapter
    private val gittoViewModel: GittoViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    private var didAppStarted = AtomicBoolean(false)

    private lateinit var lastUpdatedDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reading/check data from database
        gittoViewModel.readDataFromDB()

        sharedPreferences = getSharedPreferences("GITTO_PREF", Context.MODE_PRIVATE)

        gittoViewModel.getRepository().gittoLiveData.observe(this, Observer {
            gittoViewModel.insertItemToDB(it)
            gittoViewModel.readDataFromDB()
        })

        checkAppStartUp()



        Log.d("TAG_J", "onCreate: mainActivity")

        //ViewPager Adapter
        gittoViewPagerAdapter = GittoViewPagerAdapter(supportFragmentManager)
        binding.mainViewPager.adapter = gittoViewPagerAdapter
        binding.mainViewPager.addOnPageChangeListener(this)
        binding.mainNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_menu_item -> binding.mainViewPager.currentItem = 0
                R.id.add_menu_item -> binding.mainViewPager.currentItem = 1
                R.id.settings_menu_item -> binding.mainViewPager.currentItem = 2
            }
            true
        }


    }

    private fun checkAppStartUp() {

        //TODO: Fix splash call - it is being called after gitgub login
        // Tracking the if the app was opened before
        if (!sharedPreferences.getBoolean("APP_STARTED", true)) {
            Toast.makeText(this, "Welcome back", Toast.LENGTH_LONG).show();
            callLoginScreenFragment()
            sharedPreferences.edit().putBoolean("FIRST_TIME", false).apply()

        } else{
//            Toast.makeText(this, "Welcome back", Toast.LENGTH_LONG).show();
            sharedPreferences.edit().putBoolean("FIRST_TIME", true).apply();
            callSplashScreenFragment()

    }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG_J", "onDestroy: ")

    }

    private fun callSplashScreenFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                ).add(R.id.main_frameLayout, splashScreenFragment)
                .addToBackStack(splashScreenFragment.tag)
                .commit()
    }



    private fun addUser(username: String){
        //Adding users from API to RoomDatabase
        gittoViewModel.getGitUser(username)

        //Reading all data from room database
        gittoViewModel.readDataFromDB()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //Not implemented -- not needed
    }

    override fun onPageSelected(position: Int) {
        binding.mainNavigationView.selectedItemId = when(position){
            0 -> R.id.home_menu_item
            1 -> R.id.add_menu_item
            else -> R.id.settings_menu_item
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        //Not implemented -- not needed
    }

    override fun callLoginScreenFragment() {

        val uri = intent.data

        Log.d("TAG_J", "onResume: ")
        if(uri != null && uri.toString().startsWith(GIT_REDIRECT_URI)){

            runOnUiThread {
                Toast.makeText(this, "Successfully logged in with GitHub!", Toast.LENGTH_SHORT).show()
            }

            supportFragmentManager.popBackStack()
        }else{
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                    ).replace(R.id.main_frameLayout, loginScreenFragment)
                    .addToBackStack(null)
                    .commit()
        }


    }

    override fun onBackPressed() {
        //TODO: implement backPress after successful login
    }

    override fun gitHubLogin() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$GIT_REQUEST_URL" + "?client_id=" + "$GIT_CLIENT_ID" + "&redirect_url=" + "${GIT_REDIRECT_URI}"))
        startActivity(intent)
    }
}
