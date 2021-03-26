package com.britishbroadcast.gitto.view.ui.fragment


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.LoginFragmentLayoutBinding
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_CLIENT_ID
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REDIRECT_URI
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REQUEST_URL
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.android.synthetic.main.login_fragment_layout.*

class LoginScreenFragment: Fragment() {

    private lateinit var binding: LoginFragmentLayoutBinding


    interface LoginDelegate{
        fun gitHubLogin()
        fun updateMainActivityUI()
    }

    private lateinit var loginDelegate: LoginDelegate

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            gitHubLogoImageview.setImageResource(R.drawable.github_icon)

            this.signUpTextview.setOnClickListener {
                this.signUpCardView.visibility = View.VISIBLE
            }

            this.signupWithGithubTextView.setOnClickListener {
                loginDelegate.gitHubLogin()

            }

            this.loginButton.setOnClickListener {
                if(validLogin())
                    loginUser()
            }

            this.suButton.setOnClickListener {
                if(validSignUp())
                    signUpNewUser()
            }

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginDelegate = (context as MainActivity)
    }

    private fun signUpNewUser() {
        val email = binding.suEmailEditText.text.toString().trim()
        val password = binding.suPasswordEditText.text.toString().trim()

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Sign up complete. Please verify your account through the verification email sent to you.", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()

                        val animation2 =
                                AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)

                        binding.signUpCardView.animation = animation2

                        binding.signUpCardView.visibility = View.INVISIBLE

                    } else {
                        Toast.makeText(context, "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        Log.d("TAG_J", "signUpNewUser error: ${it.exception?.localizedMessage}")

                    }
                }
    }

    private fun loginUser() {

        val email = binding.liEmailEdittext.text.toString()
        val password = binding.liPasswordEditext.text.toString()

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                            Toast.makeText(context, "User login successful!", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()
                            loginDelegate.updateMainActivityUI()

                        } else
                            Toast.makeText(context, "Please verify your account before signing in!", Toast.LENGTH_SHORT)
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
            binding.liEmailEdittext.text.isEmpty() -> {
                Toast.makeText(context, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                false
            }
            binding.liPasswordEditext.text.isEmpty() -> {
                Toast.makeText(context, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun validSignUp(): Boolean {
        when {
            binding.suEmailEditText.text.isEmpty() -> {
                Toast.makeText(context, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            binding.suPasswordEditText.text.isEmpty() -> {
                Toast.makeText(context, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            binding.suConfirmPasswordEditText.text.isEmpty() -> {
                Toast.makeText(
                        context,
                        "Confirm password cannot be empty!",
                        Toast.LENGTH_SHORT
                ).show()
                return false
            }
            (binding.suConfirmPasswordEditText.text.toString() != binding.suPasswordEditText.text.toString()) -> {
                Toast.makeText(
                        context,
                        "Password do not match",
                        Toast.LENGTH_SHORT
                )
                        .show()
                return false
            }
            else -> return true
        }
    }

    fun githubLogin(){

    }
}