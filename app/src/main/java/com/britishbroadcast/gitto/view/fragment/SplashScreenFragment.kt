package com.britishbroadcast.gitto.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.SplashScreenLayoutBinding
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_CLIENT_ID
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REDIRECT_URI
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REQUEST_URL
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment(
    private val activityIntent: Intent,
    private val encryptedSharedPreferences: SharedPreferences
) : Fragment() {


    interface SplashScreenInterface {
        fun updateMainActivityUI()
    }

    private lateinit var binding: SplashScreenLayoutBinding
    private lateinit var splashScreenInterface: SplashScreenInterface

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
        val animFadeOut = AnimationUtils.loadAnimation(thisContext, android.R.anim.fade_out)
        animFadeIn.duration = 1500
        animFadeOut.duration = 1500
        val uri = activityIntent.data


        //*
        //* GitHub Sing-in/Registration check point
        //*

        // If the user has just sign-in or registered through github
        // Chooser layout will be dismissed and the user will be directed to the home screen
        if (uri != null && uri.toString().startsWith(GIT_REDIRECT_URI)) {
            encryptedSharedPreferences.edit()
                .putString("GIT_HUB_TOKEN", uri.getQueryParameter("code")).apply()
            Toast.makeText(context, "Successfully sign-in with GitHub!", Toast.LENGTH_SHORT).show()
            splashScreenInterface.updateMainActivityUI()
        } else {
            // If not, app logo animation will happen and chooser layout will be presented after that
            lifecycleScope.launch {

                // Logo animation section
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
                        animFadeOut
                    binding.logoImageView.visibility = View.GONE
                    delay(1000)
                }
                endLogoAni.join()

                Log.d("TAG_J", "animation have ended")

                // Only show chooser section if no 'currentUser'
                if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true || encryptedSharedPreferences.getString(
                        "GIT_HUB_TOKEN",
                        ""
                    ) != ""
                ) {
                    Toast.makeText(context, "Welcome back", Toast.LENGTH_SHORT).show()
                    closeSplashScreenToMainActivity()
                } else {
                    callChooserLayout(animFadeIn)
                }


                //*
                //* Chooser Layout Section
                //*


                //* Sign-in section

                // Calling sign-in layout from chooser
                binding.loginButton.setOnClickListener {
                    lifecycleScope.launch {

                        val dismissChooserAni: Job = launch {
                            binding.chooserLayout.animation =
                                animFadeOut
                            binding.chooserLayout.visibility = View.INVISIBLE
                            delay(1000)
                        }
                        dismissChooserAni.join()

                        callLoginLayout(animFadeIn)
                    }
                }

                // Dismissing sign-in layout and going back to chooser layout
                binding.backIconImageView.setOnClickListener {
                    lifecycleScope.launch {

                        val dismissSignInLayout: Job = launch {
                            binding.singInLayout.animation =
                                animFadeOut
                            binding.singInLayout.visibility = View.INVISIBLE
                            delay(1000)
                        }

                        dismissSignInLayout.join()
                        callChooserLayout(animFadeIn)

                    }
                }

                // Sign-in credentials check
                // If successful, it will bring the user to the HomeScreen
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


                //* Sign-Up/User registration section

                // Calling register layout from chooser
                binding.registerButton.setOnClickListener {
                    lifecycleScope.launch {

                        val dismissChooserAni: Job = launch {
                            binding.chooserLayout.animation =
                                animFadeOut
                            binding.chooserLayout.visibility = View.INVISIBLE
                            delay(1000)
                        }
                        dismissChooserAni.join()
                        callRegisterLayout(animFadeIn)
                    }
                }

                // Dismissing register layout and going back to chooser layout
                binding.regBackIconImageView.setOnClickListener {
                    lifecycleScope.launch {

                        val dismissRegisterLayout: Job = launch {
                            binding.registerLayout.animation =
                                animFadeOut
                            binding.registerLayout.visibility = View.INVISIBLE
                            delay(1000)
                        }

                        dismissRegisterLayout.join()
                        callChooserLayout(animFadeIn)

                    }
                }

                // Calling register layout from sign-in layout
                binding.registerTextView.setOnClickListener {
                    lifecycleScope.launch {

                        val dismissSignInLayout: Job = launch {
                            binding.singInLayout.animation =
                                animFadeOut
                            binding.singInLayout.visibility = View.INVISIBLE
                            delay(1000)
                        }

                        dismissSignInLayout.join()
                        callRegisterLayout(animFadeIn)

                    }
                }

                // Firebase email/password register flow
                binding.regButton.setOnClickListener {
                    // Validate edit texts
                    if (validSignUp()) {
                        signUpNewUser()
                    }
                    // Observers for firebase task completion
                    registerLiveData.observe(viewLifecycleOwner, Observer { task ->
                        lifecycleScope.launch {
                            task.addOnCompleteListener {
//                            Log.d("TAG_J", "Observer of registerLiveData value: ${it.isSuccessful}")
                                if (it.isSuccessful) {
                                    // Successful account creation triggers email verification to be sent
                                    // Dismisses registration layout
                                    // and bring the user to the successful account creation layout
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

                // Dismiss success registration layout and brings to the sign in section
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

                //TODO: implement view password filed through the eyeImageView


                //* GitHub sign-in/registration flow starter section

                binding.gitHubIconCardView.setOnClickListener {
                    startGitHubAlt()
                }

                binding.regGitHubIconCardView.setOnClickListener {
                    startGitHubAlt()
                }

            }
        }


    }

    private fun startGitHubAlt() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("$GIT_REQUEST_URL" + "?client_id=" + "$GIT_CLIENT_ID" + "&redirect_url=" + "${GIT_REDIRECT_URI}")
        )
        startActivity(intent)
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
                        context, "Sorry. ${it.exception?.localizedMessage}",
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


    private fun closeSplashScreenToMainActivity() {
        splashScreenInterface.updateMainActivityUI()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        splashScreenInterface = (context as MainActivity)
        thisContext = context
    }


}