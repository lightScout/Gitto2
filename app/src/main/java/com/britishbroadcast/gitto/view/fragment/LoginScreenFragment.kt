package com.britishbroadcast.gitto.view.ui.fragment


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment_layout.*

class LoginScreenFragment: Fragment() {

    private lateinit var binding: LoginFragmentLayoutBinding


    interface LoginDelegate{
        fun gitHubLogin()
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
                        Toast.makeText(context, "Sign up complete.", Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()

                        val animation2 =
                                AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)

                        binding.signUpCardView.animation = animation2

                        binding.signUpCardView.visibility = View.GONE
                    } else {
                        Toast.makeText(
                                context,
                                "Error1 ${it.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT
                        ).show()

                    }
                }
    }

    private fun loginUser() {
        val email = li_email_edittext.text.toString()
        val password = li_password_editext.text.toString()

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                            Toast.makeText(context, "User authenticated!", Toast.LENGTH_SHORT).show()
                        } else
                            Toast.makeText(context, "Please verify your Email!", Toast.LENGTH_SHORT)
                                    .show()
                    } else {
                        Toast.makeText(
                                context,
                                it.exception?.localizedMessage ?: "Something went wrong",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }

    private fun validLogin(): Boolean {
        when {
            li_email_edittext.text.isEmpty() -> {
                Toast.makeText(context, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            li_password_editext.text.isEmpty() -> {
                Toast.makeText(context, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    private fun validSignUp(): Boolean {
        when {
            su_email_editText.text.isEmpty() -> {
                Toast.makeText(context, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            su_password_editText.text.isEmpty() -> {
                Toast.makeText(context, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
            su_confirm_password_editText.text.isEmpty() -> {
                Toast.makeText(
                        context,
                        "Confirm password cannot be empty!",
                        Toast.LENGTH_SHORT
                ).show()
                return false
            }
            su_confirm_password_editText != su_password_editText -> {
                Toast.makeText(
                        context,
                        "Confirm password must match password!",
                        Toast.LENGTH_SHORT
                )
                        .show()
                return false
            }
            else -> return true
        }
    }


}


