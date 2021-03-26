package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.SplashScreenLayoutBinding
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.view.ui.fragment.LoginScreenFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.util.*

class SplashScreenFragment: Fragment() {



    interface SplashScreenInterface{
        fun checkAppStartUp()
        fun updateMainActivityUI()
        fun callLoginScreenFragment()
    }

    private lateinit var binding: SplashScreenLayoutBinding
    private lateinit var splashScreenInterface: SplashScreenInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SplashScreenLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            logoImageView.setImageResource(R.drawable.logo)
        }
        Log.d("TAG_J", "onViewCreated: splah!")

//        Coroutine
        GlobalScope.async {
            delay(3000L)
            async {
                if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                    Log.d("TAG_J", "FIrebase auth true")
                    parentFragmentManager.popBackStack()
                    splashScreenInterface.updateMainActivityUI()
                } else {
                    parentFragmentManager.popBackStack()
                    splashScreenInterface.callLoginScreenFragment()
                }
            }

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        splashScreenInterface = (context as MainActivity)
    }


}