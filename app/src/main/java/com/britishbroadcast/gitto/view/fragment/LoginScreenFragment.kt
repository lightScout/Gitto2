package com.britishbroadcast.gitto.view.ui.fragment


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_CLIENT_ID
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REDIRECT_URI
import com.britishbroadcast.gitto.util.Constants.Companion.GIT_REQUEST_URL
import com.britishbroadcast.gitto.view.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginScreenFragment: Fragment() {
//
//    private lateinit var binding: LoginFragmentLayoutBinding
//
//
//    interface LoginDelegate{
//        fun updateMainActivityUI()
//    }
//
//    private lateinit var loginDelegate: LoginDelegate
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        binding = LoginFragmentLayoutBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.apply {
//            gitHubLogoImageview.setImageResource(R.drawable.github_icon)
//
//            this.signUpTextview.setOnClickListener {
//                this.signUpCardView.visibility = View.VISIBLE
//            }
//
//            this.signupWithGithubTextView.setOnClickListener {
//                gitHubLogin()
//
//            }
//
//            this.loginButton.setOnClickListener {
//                if(validLogin())
//                    loginUser()
//            }
//
//            this.suButton.setOnClickListener {
//                if(validSignUp())
//                    signUpNewUser()
//            }
//
//        }
//
//    }
//
//    private fun gitHubLogin() {
//
//
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$GIT_REQUEST_URL" + "?client_id=" + "$GIT_CLIENT_ID" + "&redirect_url=" + "${GIT_REDIRECT_URI}"))
//        startActivity(intent)
//
////        val provider = OAuthProvider.newBuilder("github.com")
////        val scopes: List<String> = listOf("user","repo")
////        provider.scopes = scopes
////
////        FirebaseAuth.getInstance().pendingAuthResult?.let {
////            it
////                .addOnSuccessListener {
////                    if (it != null) {
////                        Log.d("TAG_J", "Welcome Back!")
////                    }
////                }
////                .addOnFailureListener {
////                    Log.d("TAG_J", "gitHubLogin, something went wrong: ${it.localizedMessage}")
////                }
////        } ?: FirebaseAuth.getInstance().startActivityForSignInWithProvider((context as MainActivity), provider.build())
////            .addOnSuccessListener {
////                Log.d("TAG_J", "gitHubLogin with firebase true: ${it.credential}")
////            }
////            .addOnFailureListener {
////                Log.d("TAG_J", "gitHubLogin with firebase false: ${it.localizedMessage}")
////            }
//
//
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        loginDelegate = (context as MainActivity)
//    }
//

//

//

//

//
//    fun githubLogin(){

//    }
}