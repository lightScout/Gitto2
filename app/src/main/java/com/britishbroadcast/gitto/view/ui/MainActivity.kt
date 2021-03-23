package com.britishbroadcast.gitto.view.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.model.data.Owner
import com.britishbroadcast.gitto.view.adapter.GittoViewPagerAdapter
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import java.util.*


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    private val splashScreenFragment = SplashScreenFragment()
    private lateinit var binding: ActivityMainBinding
    private lateinit var gittoViewPagerAdapter: GittoViewPagerAdapter
    private val gittoViewModel: GittoViewModel by viewModels()

    private lateinit var lastUpdatedDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Reading all data from room database
        gittoViewModel.readDataFromDB()


        //Adding users from API to RoomDatabase
        checkIfUserExistAndAdd("lightScout")
        checkIfUserExistAndAdd("sifatsaif95")
        checkIfUserExistAndAdd("MindaRah")


        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                ).replace(R.id.main_frameLayout, splashScreenFragment)
                .addToBackStack(null)
                .commit()

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

    private fun checkIfUserExistAndAdd(username: String){
        //Reading all data from room database
        gittoViewModel.readDataFromDB()

        var ownerList = mutableListOf<String>()
        gittoViewModel.gitResponseLiveData.observe(this, Observer {
            it.forEach { gitResponse ->
                ownerList.add(gitResponse[0].owner.login)
                Log.d("TAG_X", gitResponse[0].owner.login)
            }
        })

        if(ownerList.isEmpty()){
            addUser(username)
        }else{
            var isAlreadyInDB = false
            ownerList.forEach {
                if (it == username)
                    isAlreadyInDB = true
            }
            if (isAlreadyInDB)
                addUser(username)
        }
    }

    private fun addUser(username: String){
        //Adding users from API to RoomDatabase
        gittoViewModel.getGitUser(username)
        gittoViewModel.getRepository().gittoLiveData.observe(this, Observer {
            Log.d("TAG_X", it)
            gittoViewModel.insertItemToDB(it)
        })
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
}
