package com.vazheninapps.githubuserwatcher.screens.fragments

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.vazheninapps.githubuserwatcher.api.ApiFactory
import com.vazheninapps.githubuserwatcher.api.AuthBody
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LoginViewModel constructor(application: Application) : AndroidViewModel(application) {

    private val db = UserDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    val isLoginSuccess = MutableLiveData<Boolean>()
    val isUserSuccess = MutableLiveData<Boolean>()

    fun basicLogin(context: Context, login: String, password: String): Boolean {
        if (ApiFactory.isInternetConnection(getApplication())) {
            val disposable = ApiFactory.getFactoryWithCredentials(login,password)
                .loginService!!
                .createAuthorizationToken(AuthBody.generate())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.body() != null) {
                        LoggedUser.token = "token " + it.body()!!.token
                        context.getSharedPreferences("main", Context.MODE_PRIVATE).edit()
                            .putString("token", LoggedUser.token).apply()
                        isLoginSuccess.postValue(true)
                    } else {
                        isLoginSuccess.postValue(false)
                    }
                }, {
                    isLoginSuccess.postValue(false)
                })
            compositeDisposable.add(disposable)
            return true
        } else {
            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun loadLoggedUser(context: Context) {
        if (ApiFactory.isInternetConnection(getApplication())) {
            val disposable: Disposable = ApiFactory
                .getInstance()
                .userService!!
                .getLoggedUser(LoggedUser.token)
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.userDao().insertUserDetailed(it)
                    LoggedUser.id = it.id
                    context.getSharedPreferences("main", Context.MODE_PRIVATE).edit()
                        .putInt("id", LoggedUser.id!!).apply()
                    isUserSuccess.postValue(true)
                }, {
                })
            compositeDisposable.add(disposable)
        } else {
            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
        }

    }

    fun handleAuthByWeb(context: Context, fromWebUri: String){
      val uri = Uri.parse(fromWebUri)

        uri?.let{
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")

            if (ApiFactory.isInternetConnection(getApplication())) {
                val disposable = ApiFactory
                    .getNotApiFactory()
                    .loginService!!
                    .loadWebToken(AuthBody.CLIENT_ID, AuthBody.CLIENT_SECRET, code, state)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.d("111222", it.toString())
                        if (it.body() != null) {
                            LoggedUser.token = "token " + it.body()!!.accessToken
                            context.getSharedPreferences("main", Context.MODE_PRIVATE).edit()
                                .putString("token", LoggedUser.token).apply()
                            isLoginSuccess.postValue(true)
                        } else {
                            isLoginSuccess.postValue(false)
                        }
                    }, {
                        isLoginSuccess.postValue(false)
                    })
                compositeDisposable.add(disposable)
            } else {
                Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
            }

    }}

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}