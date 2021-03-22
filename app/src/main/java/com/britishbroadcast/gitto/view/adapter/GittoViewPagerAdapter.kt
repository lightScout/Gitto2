package com.britishbroadcast.gitto.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.britishbroadcast.gitto.view.ui.AddFragment
import com.britishbroadcast.gitto.view.ui.HomeFragment
import com.britishbroadcast.gitto.view.ui.SettingsFragment

class GittoViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    companion object{
        const val FRAGMENT_COUNT = 3
    }

    override fun getCount(): Int = FRAGMENT_COUNT

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> AddFragment()
            else -> SettingsFragment()
        }
    }
}