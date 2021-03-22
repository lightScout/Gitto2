package com.britishbroadcast.gitto.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.viewmodel.GittoViewModel

class LoginScreenFragment: Fragment() {

    companion object {
        fun newInstance(): LoginScreenFragment {
            return LoginScreenFragment()
        }
    }

    interface OnLoginSelected {
        fun onLoginSelected()
    }

    private val gittoViewModel by activityViewModels<GittoViewModel>()



    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context != null) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment_layout, container, false)
    }




}