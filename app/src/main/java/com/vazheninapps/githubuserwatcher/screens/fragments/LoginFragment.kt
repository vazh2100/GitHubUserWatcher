package com.vazheninapps.githubuserwatcher.screens.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.api.AuthBody
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onResume() {
        super.onResume()
        arguments?.let {
            if(it.containsKey("uri")){
            buttonLogin.isEnabled = false
            progressBarLoading.visibility = View.VISIBLE
            viewModel.handleAuthByWeb(requireActivity(), it.getString("uri", ""))}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipLoginIfLogged()
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        setListenersOnButtons()
        observeLoginSuccess()
    }

    private fun skipLoginIfLogged(){
        if (LoggedUser.token != null && LoggedUser.id != null && LoggedUser.id != 0) {
            findNavController().navigate(R.id.action_loginFragment_to_main_graph)
        }
    }


   private fun setListenersOnButtons(){
       buttonLogin.setOnClickListener {
           val login = editTextLogin.text.toString().trim()
           val password = editTextPassword.text.toString()

           if (isLoginInfoCorrect(login, password)) {
               buttonLogin.isEnabled = false
               progressBarLoading.visibility = View.VISIBLE
               viewModel.basicLogin(requireActivity(), login, password)
           }
       }
       textViewWebLogin.setOnClickListener {
           startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AuthBody.getUrlToWebAuth())).addCategory(Intent.CATEGORY_BROWSABLE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
       }

   }

    private fun isLoginInfoCorrect (login: String, password: String): Boolean {
        return when {
            login.isEmpty() && password.isEmpty() -> {
                showToast("Введите логин и пароль")
                false
            }
            login.isEmpty() -> {
                showToast("Введите логин")
                false
            }
            password.isEmpty() -> {
                showToast("Введите пароль")
                false
            }
            else -> true
        }
    }

   private fun observeLoginSuccess(){
       viewModel.getErrors().observe(requireActivity(), {
           it?.let{showToast(it)
               buttonLogin?.isEnabled = true
               progressBarLoading?.visibility = View.GONE}
       })
       viewModel.getIsLoginSuccess().observe(requireActivity(),{
           it?.let{
               if (it && findNavController().currentDestination?.id == R.id.loginFragment ){
                   findNavController().navigate(R.id.action_loginFragment_to_main_graph)
               }
           }
       })
   }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }



}