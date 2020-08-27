package com.vazheninapps.githubuserwatcher.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipLoginIfLogged()
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        setListenerOnLoginButton()
        observeLoginSuccess()

    }

    private fun skipLoginIfLogged(){
        if (LoggedUser.token != null && LoggedUser.id != null && LoggedUser.id != 0) {
            findNavController().navigate(R.id.action_loginFragment_to_main_graph)
        }
    }


   private fun setListenerOnLoginButton(){
       buttonLogin.setOnClickListener {
           val login = editTextLogin.text.toString().trim()
           val password = editTextPassword.text.toString()
           if (isLoginInfoCorrect(login, password)) {
               buttonLogin.isEnabled = false
               progressBarLoading.visibility = View.VISIBLE
             val connection =  viewModel.basicLogin(requireActivity(), login, password)
               if(!connection){
                   buttonLogin.isEnabled = true
                   progressBarLoading.visibility = View.GONE
               }
           } else {
               buttonLogin.isEnabled = true
           }
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
       viewModel.isUserSuccess.value = null
       viewModel.isLoginSuccess.value = null
       viewModel.isUserSuccess.observe(requireActivity(), {
           it?.let {
               if (it) {
                   if (findNavController().currentDestination?.id == R.id.loginFragment) {
                       findNavController().navigate(R.id.action_loginFragment_to_main_graph)
                   }
               } else {
                   progressBarLoading?.visibility = View.GONE
                   buttonLogin.isEnabled = true
                   showToast("Не удалось загрузить информацию о пользователе. Попробуйте снова")
               }
           }

       })
       viewModel.isLoginSuccess.observe(requireActivity(), {
           it?.let {
               if (it) {
                   progressBarLoading?.visibility = View.GONE
                   viewModel.loadLoggedUser(requireActivity())
               } else {
                   progressBarLoading?.visibility = View.GONE
                   buttonLogin.isEnabled = true
                   showToast("Неправильный логин или пароль")
               }
           }
       })

   }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


}