package com.britishbroadcast.gitto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.SplashScreenLayoutBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SplashScreenFragment: Fragment() {

    private lateinit var binding: SplashScreenLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SplashScreenLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            logoImageView.setImageResource(R.drawable.logo)
        }

//        //Coroutine
//        GlobalScope.launch {
//            delay(2000L)
//            // Thi is where we will call the login page
//            // For now it is only popBackStack
//            parentFragmentManager.popBackStack()
//        }


    }


}