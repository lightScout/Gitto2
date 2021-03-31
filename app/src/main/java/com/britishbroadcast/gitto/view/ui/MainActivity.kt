package com.britishbroadcast.gitto.view.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.viewpager.widget.ViewPager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.view.adapter.GittoViewPagerAdapter
import com.britishbroadcast.gitto.view.fragment.RepositoriesFragment
import com.britishbroadcast.gitto.view.fragment.SettingsFragment
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.security.GeneralSecurityException
import java.time.LocalDateTime


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    SplashScreenFragment.SplashScreenInterface, SettingsFragment.SettingsDelegate {
    private lateinit var splashScreenFragment: SplashScreenFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var gittoViewPagerAdapter: GittoViewPagerAdapter
    private val gittoViewModel: GittoViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var encryptedSharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("GITTO_PREF", Context.MODE_PRIVATE)

        initializeEncryptedSP()

        // Only calling splash screen after encryptedSP is initializes
        if (this::encryptedSharedPreferences.isInitialized) {
            splashScreenFragment = SplashScreenFragment(intent, encryptedSharedPreferences, false)
            callSplashScreenFragment()
        }


        //ViewPager Adapter
        gittoViewPagerAdapter = GittoViewPagerAdapter(supportFragmentManager)
        binding.mainViewPager.adapter = gittoViewPagerAdapter
        binding.mainViewPager.addOnPageChangeListener(this)
        binding.mainNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_menu_item -> binding.mainViewPager.currentItem = 0
                R.id.add_menu_item -> binding.mainViewPager.currentItem = 1
                R.id.settings_menu_item -> binding.mainViewPager.currentItem = 2
            }
            true
        }


    }

    private fun initializeEncryptedSP() {
        try {
            // Creating Master Key for encryption
            val masterKey =
                MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

            encryptedSharedPreferences = EncryptedSharedPreferences.create(
                this,
                packageName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM

            )
        } catch (e: GeneralSecurityException) {
            e.printStackTrace();
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkLastApiCall(userName: String) {
        val prevDate = sharedPreferences.getString("DATE_PREF", "")
        val currentDate = LocalDateTime.now()
        Log.d("TAG_J", "prevDate: ${prevDate.isNullOrEmpty()}")
        if (prevDate.isNullOrEmpty()) {
            Log.d("TAG_J", "checkLastApiCall: timer set")
            sharedPreferences.edit().putString("DATE_PREF", currentDate.toString()).apply()
            gittoViewModel.populateDB(userName)
        } else {
            Log.d("TAG_J", "checkLastApiCall: checking timer")
            val prevDateTime = LocalDateTime.parse(prevDate)
            val diff = currentDate.minusHours(24)
            if (prevDateTime <= diff) {
                Log.d("TAG_J", "checkLastApiCall: 24h has pass set")
                gittoViewModel.getRepository().updateDataBase()

            } else {
                Log.d("TAG_J", "checkLastApiCall: 24h since last api call has not past")
                gittoViewModel.getRepository().gitResponseLiveData.postValue(gittoViewModel.getAllDataFromDB())
            }

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


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //Not implemented -- not needed
    }

    override fun onPageSelected(position: Int) {
        binding.mainNavigationView.selectedItemId = when (position) {
            0 -> R.id.home_menu_item
            1 -> R.id.add_menu_item
            else -> R.id.settings_menu_item
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        //Not implemented -- not needed
    }


    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack()
        }
        ft.commit()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateMainActivityUI(userName: String) {
        binding.mainFrameLayout.animation =
            AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        binding.mainFrameLayout.visibility = View.GONE


        Log.d("TAG_J", "updateMainActivityUI: set visibility $this")
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainViewPager.visibility = View.VISIBLE

        checkLastApiCall(userName)
    }



    override fun logout() {
        Log.d("TAG_J", "logout: ")
        binding.mainViewPager.animation =  AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        binding.mainNavigationView.animation =  AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        binding.mainViewPager.visibility = View.INVISIBLE
        binding.mainNavigationView.visibility = View.INVISIBLE
        Handler(mainLooper).postDelayed({
            binding.mainFrameLayout.animation =
                AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            binding.mainFrameLayout.visibility = View.VISIBLE

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            val newSplashScreenFragment =
                SplashScreenFragment(intent, encryptedSharedPreferences, true)

            FirebaseAuth.getInstance().signOut()

            var cleanDone = false
            lifecycleScope.launch {
                Log.d("TAG_J", "logout: coroutineScope")
                while (!cleanDone) {
                    Log.d("TAG_J", "cleanDone: $cleanDone")
                    val job: Deferred<Boolean> = async {
                        encryptedSharedPreferences.edit()
                            .putString("GIT_HUB_TOKEN", "").apply()
                        sharedPreferences.edit().putString("DATE_PREF", "").apply()
                        true
                    }
                    cleanDone = job.await()
                }
                Log.d("TAG_J", "cleanDone: $cleanDone")
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    ).replace(R.id.main_frameLayout, newSplashScreenFragment)
                    .addToBackStack(splashScreenFragment.tag)
                    .commit()

            }



            gittoViewModel.getRepository().clearDB()
        }, 2000)

    }


}
