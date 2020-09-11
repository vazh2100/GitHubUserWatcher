package com.vazheninapps.githubuserwatcher.screens.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.api.ApiFactoryMain
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import com.vazheninapps.githubuserwatcher.utils.NetworkUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserViewModel constructor(application: Application) : AndroidViewModel(application) {


    private val db by lazy {UserDatabase.getInstance(application)}
    private val compositeDisposable by lazy { CompositeDisposable()}
    private val userListLD by lazy {db.userDao().getUsers().also {loadUsers()} }
    private val errorsLD by lazy { MutableLiveData<String>() }
    fun getErrors ():LiveData<String>{
        return errorsLD.also {errorsLD.value = null }
    }

   fun getUsers():LiveData<List<User>> {
       return  userListLD
   }



    fun getLoggedUser(id: Int?): LiveData<UserDetailed> {
        return db.userDao().getUserDetailed(id)
    }
    fun getUserDetailed(id: Int?, login:String?): LiveData<UserDetailed> {
        return db.userDao().getUserDetailed(id).also {loadUserDetailed(login) }
    }

    fun loadUsers(since: Int = 0) {
        if (NetworkUtils.isInternetConnection(getApplication())) {
            val disposable: Disposable = ApiFactoryMain
                .userService
                .getUsers(since, LoggedUser.token)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.userDao().insertUsers(it)
                }, {
                    errorsLD.postValue(it.message.toString())
                })
            compositeDisposable.add(disposable)
        } else {
            errorsLD.postValue(getApplication<Application>().getString(R.string.message_no_internet_connection))
        }
    }

   private fun loadUserDetailed(login: String?) {
        if (NetworkUtils.isInternetConnection(getApplication())) {

            val disposable: Disposable = ApiFactoryMain
                .userService
                .getUserDetail(login, LoggedUser.token)
                .subscribeOn(Schedulers.io())
                .subscribe({ db.userDao().insertUserDetailed(it)
                    Log.d("11112222", "Загрузка произошла")
                   }, {
                    errorsLD.postValue(it.message.toString())
                    Log.d("11112222", "${LoggedUser.token}\n ${LoggedUser.id}")
                })
            compositeDisposable.add(disposable)
        } else {
            errorsLD.postValue(getApplication<Application>().getString(R.string.message_no_internet_connection))
        }
    }


// Метод, объединяющий 2 предыдущих метода на случай если разрешённое  количество запросов в час станет больше
//    fun loadUserFull(since: Int = 0){
//        if (ApiFactory.isInternetConnection(getApplication())) {
//            val disposable = ApiFactory
//                .getInstance()
//                .apiService
//                .getUsers(since)
//                .flatMapObservable { Observable.fromIterable(it) }
//                .flatMapSingle { ApiFactory.getInstance().apiService.getUserDetail(it.login)}
//                .toList()
//                .retry()
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                  Вставка в базу данных списка
//                }, {
//                    Log.d("TEST", it.message)
//                })
//            compositeDisposable.add(disposable)
//        } else {
//            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}