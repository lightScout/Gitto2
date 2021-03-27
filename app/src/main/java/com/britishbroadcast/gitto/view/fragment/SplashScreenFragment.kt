package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.SplashScreenLayoutBinding
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashScreenFragment : Fragment() {


    interface SplashScreenInterface {
        fun checkAppStartUp()
        fun updateMainActivityUI()
    }

    private lateinit var binding: SplashScreenLayoutBinding
    private lateinit var splashScreenInterface: SplashScreenInterface
    private val gittoViewModel by activityViewModels<GittoViewModel>()

    private var logoAnimEnded = false
    private lateinit var thisContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SplashScreenLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var animFadeIn = AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_in)
        animFadeIn.duration = 1000


        lifecycleScope.launch {
            binding.logoImageView.setImageResource(R.drawable.logo)
            val startLogAni: Job = launch {
                Log.d("TAG_J", "start animation in progress")

                binding.logoImageView.animation =
                    AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_in)
                binding.logoImageView.visibility = View.VISIBLE
                delay(3000)
            }
            startLogAni.join()

            val endLogoAni: Job = launch {
                Log.d("TAG_J", "end animation in progress")
                binding.logoImageView.animation =
                    AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
                binding.logoImageView.visibility = View.GONE
                delay(1000)
            }
            endLogoAni.join()

            Log.d("TAG_J", "animation have ended")

            // Only show login section if no 'currentUser'
            FirebaseAuth.getInstance().currentUser?.let {
                closeSplashScreenToMainActivity()
            } ?: {
                callChooserLayout(animFadeIn)
            }()

            // Calling sign in layout
            binding.loginButton.setOnClickListener {
                lifecycleScope.launch {

                    val dismissChooserAni: Job = launch {
                        binding.chooserLayout.animation =
                            AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
                        binding.chooserLayout.visibility = View.INVISIBLE
                        delay(1000)
                    }
                    dismissChooserAni.join()

                    callLoginLayout(animFadeIn)
                }
            }

            binding.backIconImageView.setOnClickListener {
                lifecycleScope.launch {

                    val dismissSignInLayout: Job = launch {
                        binding.singInLayout.animation =
                            AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
                        binding.singInLayout.visibility = View.INVISIBLE
                        delay(1000)
                    }

                    dismissSignInLayout.join()
                    callChooserLayout(animFadeIn)

                }
            }

        }


    }

    private fun callChooserLayout(animFadeIn: Animation) {
        binding.chooserLayout.animation = animFadeIn
        binding.chooserLayout.visibility = View.VISIBLE

    }

    private fun callLoginLayout(animFadeIn: Animation) {
        binding.singInLayout.animation = animFadeIn
        binding.singInLayout.visibility = View.VISIBLE


    }

    private fun checkPendingResult() {
        var animFadeIn = AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
//        binding.loginLayout =
    }

    private fun closeSplashScreenToMainActivity() {
        TODO("Not yet implemented")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        splashScreenInterface = (context as MainActivity)
        thisContext = context
    }


}