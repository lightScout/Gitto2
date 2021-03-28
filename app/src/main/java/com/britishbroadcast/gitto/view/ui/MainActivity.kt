package com.britishbroadcast.gitto.view.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.viewpager.widget.ViewPager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.view.adapter.GittoViewPagerAdapter
import com.britishbroadcast.gitto.view.fragment.RepositoriesFragment
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import java.io.IOException
import java.security.GeneralSecurityException
import java.time.LocalDateTime


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    SplashScreenFragment.SplashScreenInterface, RepositoriesFragment.RepositoryInterface {
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
            splashScreenFragment = SplashScreenFragment(intent, encryptedSharedPreferences)
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
    private fun checkLastApiCall() {
        val prevDate = sharedPreferences.getString("DATE_PREF", "")
        val currentDate = LocalDateTime.now()
        Log.d("TAG_J", "prevDate: ${prevDate.isNullOrEmpty()}")
        if (prevDate.isNullOrEmpty()) {
            Log.d("TAG_J", "checkLastApiCall: timer set")
            sharedPreferences.edit().putString("DATE_PREF", currentDate.toString()).apply()
            gittoViewModel.populateDB()
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
    override fun updateMainActivityUI() {
        binding.mainFrameLayout.animation =
            AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        binding.mainFrameLayout.visibility = View.GONE


        Log.d("TAG_J", "updateMainActivityUI: set visibility $this")
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainViewPager.visibility = View.VISIBLE

        checkLastApiCall()
    }


}
