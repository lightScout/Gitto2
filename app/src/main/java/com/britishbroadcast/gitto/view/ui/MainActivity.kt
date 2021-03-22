package com.britishbroadcast.gitto.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.ActivityMainBinding
import com.britishbroadcast.gitto.model.network.GittoRetrofit
import com.britishbroadcast.gitto.view.fragment.SplashScreenFragment
import com.britishbroadcast.gitto.view.adapter.GittoViewPagerAdapter
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    private val gittoViewModel = GittoViewModel()
    private val splashScreenFragment = SplashScreenFragment()
    private lateinit var binding: ActivityMainBinding
    private lateinit var gittoViewPagerAdapter: GittoViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
