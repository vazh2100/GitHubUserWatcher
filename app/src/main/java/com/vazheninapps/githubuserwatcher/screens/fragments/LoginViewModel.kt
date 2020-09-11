package com.vazheninapps.githubuserwatcher.screens.fragments

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.api.ApiFactoryMain
import com.vazheninapps.githubuserwatcher.api.ApiFactoryNoApi
import com.vazheninapps.githubuserwatcher.api.ApiFactoryWithCredentials
import com.vazheninapps.githubuserwatcher.api.AuthBody
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import com.vazheninapps.githubuserwatcher.utils.NetworkUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LoginViewModel constructor(application: Application) : AndroidViewModel(application) {

    private val db by lazy { UserDatabase.getInstance(application) }
    private val compositeDisposable by lazy {CompositeDisposable()}
    private val isLoginSuccess by lazy { MutableLiveData<Boolean>() }

    private val errorsLD by lazy { MutableLiveData<String>() }

    fun getErrors ():LiveData<String>{
       return errorsLD.also {errorsLD.value = null }
   }

    fun getIsLoginSuccess():LiveData<Boolean>{
        return isLoginSuccess.also{isLoginSuccess.value = null}
    }

    fun basicLogin(context: Context, login: String, password: String) {
        if (NetworkUtils.isInternetConnection(getApplication())) {
            val disposable = ApiFactoryWithCredentials
                .init(login, password)
                .loginServiceBase
                .createAuthorizationToken(AuthBody.generate())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.body() != null) {
                        LoggedUser.token = "token " + it.body()!!.token
                        context.getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString("token", LoggedUser.token).apply()
                        loadLoggedUser(context)
                    } else {
                        errorsLD.postValue(context.getString(R.string.message_wrong_login_or_password))
                    }
                }, {
                    errorsLD.postValue(context.getString(R.string.message_failed_connect_to_server))
                })
            compositeDisposable.add(disposable)
        } else {
            errorsLD.value = context.getString(R.string.message_no_internet_connection)
        }
    }


    fun handleAuthByWeb(context: Context, fromWebUri: String){
        val uri = Uri.parse(fromWebUri)

        uri?.let{
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")

            if (NetworkUtils.isInternetConnection(getApplication())) {
                val disposable = ApiFactoryNoApi
                    .loginServiceWebFlow
                    .loadWebToken(AuthBody.CLIENT_ID, AuthBody.CLIENT_SECRET, code, state)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if (it.body()?.accessToken != null) {
                            LoggedUser.token = "token " + it.body()!!.accessToken
                            context.getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString("token", LoggedUser.token).apply()
                            loadLoggedUser(context)
                        } else {
                            errorsLD.postValue(context.getString(R.string.message_failed_get_token))
                        }
                    }, {
                        errorsLD.postValue(context.getString(R.string.message_failed_connect_to_server))
                    })
                compositeDisposable.add(disposable)
            } else {
                errorsLD.value = context.getString(R.string.message_no_internet_connection)
            }

        }}

   private fun loadLoggedUser(context: Context) {
        if (NetworkUtils.isInternetConnection(getApplication())) {
            val disposable: Disposable = ApiFactoryMain
                .userService
                .getLoggedUser(LoggedUser.token)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.userDao().insertUserDetailed(it)
                    LoggedUser.id = it.id
                    context.getSharedPreferences("main", Context.MODE_PRIVATE).edit().putInt("id", LoggedUser.id!!).apply()
                    isLoginSuccess.postValue(true)
                }, {
                    errorsLD.postValue(context.getString(R.string.failed_to_load_logged_user))
                })
            compositeDisposable.add(disposable)
        } else {
            errorsLD.value = context.getString(R.string.message_no_internet_connection)
        }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}