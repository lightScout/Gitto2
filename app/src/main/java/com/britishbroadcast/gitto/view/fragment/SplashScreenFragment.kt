package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.SplashScreenLayoutBinding
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.britishbroadcast.gitto.viewmodel.GittoViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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

    private var registerLiveData = MutableLiveData<Task<AuthResult>>()

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

        val animFadeIn = AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_in)
        val animFadeOut =  AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
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
            if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                closeSplashScreenToMainActivity()
            } else {
                callChooserLayout(animFadeIn)
            }

            // Calling sign in layout from chooser
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
            // Dismissing login layout and going back to chooser layout
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


            // Calling register layout from chooser
            binding.registerButton.setOnClickListener {
                lifecycleScope.launch {

                    val dismissChooserAni: Job = launch {
                        binding.chooserLayout.animation =
                            AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
                        binding.chooserLayout.visibility = View.INVISIBLE
                        delay(1000)
                    }
                    dismissChooserAni.join()
                    callRegisterLayout(animFadeIn)
                }
            }

            // Calling register layout from sign in layout
            binding.registerTextView.setOnClickListener {
                lifecycleScope.launch {

                    val dismissSignInLayout: Job = launch {
                        binding.singInLayout.animation =
                            AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
                        binding.singInLayout.visibility = View.INVISIBLE
                        delay(1000)
                    }

                    dismissSignInLayout.join()
                    callRegisterLayout(animFadeIn)

                }
            }


            // Dismissing register layout and going back to chooser layout
            binding.regBackIconImageView.setOnClickListener {
                lifecycleScope.launch {

                    val dismissRegisterLayout: Job = launch {
                        binding.registerLayout.animation =
                            AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
                        binding.registerLayout.visibility = View.INVISIBLE
                        delay(1000)
                    }

                    dismissRegisterLayout.join()
                    callChooserLayout(animFadeIn)

                }
            }

            // Sign in flow
            binding.signInButton.setOnClickListener {

                if (validLogin()) {
                    lifecycleScope.launch {
                        val loginUserJob: Job = launch {
                            loginUser()
                            delay(1500)
                        }
                        loginUserJob.join()
                    }

                }
            }

            // register flow
            binding.regButton.setOnClickListener {
                if (validSignUp()) {
                    signUpNewUser()
                }
                registerLiveData.observe(viewLifecycleOwner, Observer { task ->
                    lifecycleScope.launch {
                        task.addOnCompleteListener {
//                            Log.d("TAG_J", "Observer of registerLiveData value: ${it.isSuccessful}")
                            if (it.isSuccessful) {
                                FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                                lifecycleScope.launch {
                                    val dismissRegisterLayout: Job = launch {
                                        binding.registerLayout.animation =
                                            AnimationUtils.loadAnimation(
                                                thisContext,
                                                android.R.anim.fade_out
                                            )
                                        binding.registerLayout.visibility = View.INVISIBLE
                                        delay(1500)
                                    }
                                    dismissRegisterLayout.join()
                                    callRegisterSuccessLayout(animFadeIn)
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "${it.exception?.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                    }

                })


            }

            binding.continueButton.setOnClickListener {
                lifecycleScope.launch {
                    val dismissSuccessLayout = launch {
                        binding.regSuccessLayout.animation = animFadeOut
                        binding.regSuccessLayout.visibility = View.INVISIBLE
                        delay(1500)
                    }
                    dismissSuccessLayout.join()
                    callLoginLayout(animFadeIn)
                }
            }
        }
    }


    private fun callRegisterSuccessLayout(animFadeIn: Animation?) {
        binding.regSuccessLayout.animation = animFadeIn
        binding.regSuccessLayout.visibility = View.VISIBLE

    }

    private fun callRegisterLayout(animFadeIn: Animation?) {
        binding.registerLayout.animation = animFadeIn
        binding.registerLayout.visibility = View.VISIBLE
    }

    private fun callChooserLayout(animFadeIn: Animation) {
        binding.chooserLayout.animation = animFadeIn
        binding.chooserLayout.visibility = View.VISIBLE

    }

    private fun callLoginLayout(animFadeIn: Animation) {
        binding.singInLayout.animation = animFadeIn
        binding.singInLayout.visibility = View.VISIBLE


    }

    private fun loginUser() {

        val email = binding.siEmailEditText.text.toString()
        val password = binding.siPasswordEditText.text.toString()

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                        Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                        splashScreenInterface.updateMainActivityUI()

                    } else
                        Toast.makeText(
                            context,
                            "Please verify your account before signing in!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                } else {
                    Toast.makeText(
                        context, "Sorry, Something went wrong. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("TAG_J", "loginUser error: ${it.exception?.localizedMessage}")
                }
            }
    }

    private fun validLogin(): Boolean {
        return when {
            binding.siEmailEditText.text?.isEmpty() == true -> {
                Toast.makeText(context, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                false
            }
            binding.siPasswordEditText.text?.isEmpty() == true -> {
                Toast.makeText(context, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun signUpNewUser() {
        lifecycleScope.launch {
            val email = binding.regEmailEditText.text.toString().trim()
            val password = binding.regPasswordEditText.text.toString().trim()
            registerLiveData.postValue(
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            ).also {
                Log.d("TAG_J", "Poster of registerLiveData: value posted")
            }
        }

    }

    private fun validSignUp(): Boolean {
        when {
            binding.regEmailEditText.text?.isEmpty() == true -> {
                Toast.makeText(context, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            binding.regPasswordEditText.text?.isEmpty() == true -> {
                Toast.makeText(context, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            binding.regPasswordConfirmationEditText.text?.isEmpty() == true -> {
                Toast.makeText(
                    context,
                    "Confirm password cannot be empty!",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            (binding.regPasswordEditText.text.toString() != binding.regPasswordConfirmationEditText.text.toString()) -> {
                Toast.makeText(
                    context,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            }
            else -> return true
        }
    }


    private fun checkPendingResult() {
        var animFadeIn = AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
//        binding.loginLayout =
    }

    private fun closeSplashScreenToMainActivity() {
        splashScreenInterface.updateMainActivityUI()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        splashScreenInterface = (context as MainActivity)
        thisContext = context
    }


}