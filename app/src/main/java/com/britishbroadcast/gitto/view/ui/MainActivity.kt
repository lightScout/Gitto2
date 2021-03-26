package com.britishbroadcast.gitto.view.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_CLIENT_ID
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REDIRECT_URI
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REQUEST_URL
import com.britishbroadcast.gitto.view.adapter.GittoViewPagerAdapter
import com.britishbroadcast.gitto.view.fragment.RepositoriesFragment
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.view.fragment.UserFragment
import com.britishbroadcast.gitto.view.ui.fragment.LoginScreenFragment
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, SplashScreenFragment.SplashScreenInterface, LoginScreenFragment.LoginDelegate, RepositoriesFragment.RepositoryInterface, UserFragment.UserFragmentInterface {
    private val splashScreenFragment = SplashScreenFragment()
    private val loginScreenFragment = LoginScreenFragment()
    private lateinit var binding: ActivityMainBinding
    private lateinit var gittoViewPagerAdapter: GittoViewPagerAdapter
    private val gittoViewModel: GittoViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private val repositoriesFragment = RepositoriesFragment()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("GITTO_PREF", Context.MODE_PRIVATE)

        checkAppStartUp()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun checkAppStartUp() {

        val uri = intent.data
        if (uri != null && uri.toString().startsWith(GIT_REDIRECT_URI)) {

            Toast.makeText(this, "Successfully logged in with GitHub!", Toast.LENGTH_SHORT).show()
            checkLastApiCall()
        } else {
            callSplashScreenFragment()

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
            View.VISIBLE.apply {
                binding.mainNavigationView.visibility = this
                binding.mainViewPager.visibility = this
            }
        } else {
            Log.d("TAG_J", "checkLastApiCall: checking timer")
            val prevDateTime = LocalDateTime.parse(prevDate)
            val diff = currentDate.minusHours(24)
            if (prevDateTime <= diff) {
                Log.d("TAG_J", "checkLastApiCall: 24h has pass set")
                gittoViewModel.getRepository().updateDataBase()
                View.VISIBLE.apply {
                    binding.mainNavigationView.visibility = this
                    binding.mainViewPager.visibility = this
                }
            } else {
                Log.d("TAG_J", "checkLastApiCall: 24h since last api call has not past")
                updateMainActivityUI()
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
                ).replace(R.id.main_frameLayout, splashScreenFragment)
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

    override fun callLoginScreenFragment() {

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


    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack()
        }
        ft.commit()
    }

    override fun gitHubLogin() {
//
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$GIT_REQUEST_URL" + "?client_id=" + "$GIT_CLIENT_ID" + "&redirect_url=" + "${GIT_REDIRECT_URI}"))
//        startActivity(intent)
//        val provider = OAuthProvider.newBuilder("github.com")
        val provider = OAuthProvider.newBuilder("https://github.com/login/oauth/authorize")
        provider.addCustomParameter("client_id", GIT_CLIENT_ID)
        provider.addCustomParameter("redirect_uri","https://gitto-26117.firebaseapp.com/__/auth/handler")

//        val pendingResultTask: Task<AuthResult>? =

        FirebaseAuth.getInstance().pendingAuthResult?.let {
            it
                    .addOnSuccessListener {
                        if (it != null) {
                            Log.d("TAG_J", "Welcome Back!")
                        }
                    }
                    .addOnFailureListener {
                        Log.d("TAG_J", "gitHubLogin, something went wrong: ${it.localizedMessage}")
                    }
        } ?: FirebaseAuth.getInstance().startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    Log.d("TAG_J", "gitHubLogin with firebase true: ${it.credential}")
                }
                .addOnFailureListener {
                    Log.d("TAG_J", "gitHubLogin with firebase false: ${it.localizedMessage}")
                }


    }



    override fun updateMainActivityUI() {
        gittoViewModel.getRepository().gitResponseLiveData.postValue(gittoViewModel.getAllDataFromDB())
        View.VISIBLE.apply {
            binding.mainNavigationView.visibility = this
            binding.mainViewPager.visibility = this
        }
    }


    override fun displayRepositoriesFragment(login: String) {

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.home_frameLayout, repositoriesFragment.also {
                    val bundle = Bundle()
                    bundle.putString("USER", login)
                    it.arguments = bundle
                })
                .addToBackStack(repositoriesFragment.tag)
                .commit()
    }


}
