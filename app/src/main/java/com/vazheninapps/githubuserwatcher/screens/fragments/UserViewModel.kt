package com.vazheninapps.githubuserwatcher.screens.fragments

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vazheninapps.githubuserwatcher.api.ApiFactory
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserViewModel constructor(application: Application) : AndroidViewModel(application) {

    private val db = UserDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    private val userListLD by lazy {db.userDao().getUsers().also {loadUsers()} }

   fun getUsers():LiveData<List<User>> {
       return  userListLD
   }

    var isLoading = MutableLiveData<Boolean>()

    fun getLoggedUser(id: Int?): LiveData<UserDetailed> {
        return db.userDao().getUserDetailed(id)
    }
    fun getUserDetailed(id: Int?, login:String?): LiveData<UserDetailed> {
        return db.userDao().getUserDetailed(id).also {loadUserDetailed(login) }
    }

    fun loadUsers(since: Int = 0) {
        if (ApiFactory.isInternetConnection(getApplication())) {
            isLoading.value = true
            val disposable: Disposable = ApiFactory
                .getInstance()
                .userService!!
                .getUsers(since, LoggedUser.token)
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isLoading.postValue(false)
                    db.userDao().insertUsers(it)
                }, {
                    Log.d("TEST", it.message)
                })
            compositeDisposable.add(disposable)
        } else {
            isLoading.postValue(false)
            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
        }
    }

   private fun loadUserDetailed(login: String?) {
        if (ApiFactory.isInternetConnection(getApplication())) {
            isLoading.value = true
            val disposable: Disposable = ApiFactory
                .getInstance()
                .userService!!
                .getUserDetail(login, LoggedUser.token)
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({ db.userDao().insertUserDetailed(it)
                    isLoading.postValue(false)
                   }, {
                    Log.d("TEST", it.message)

                })
            compositeDisposable.add(disposable)
        } else {
            isLoading.postValue(false)
            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
        }
    }


// Метод, объединяющий 2 предыдущих метода на случай если разрешённое  количество запросов в час станет больше
//    fun loadUserFull(since: Int = 0){
//        if (ApiFactory.isInternetConnection(getApplication())) {
//            isLoading.value = true
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
//            isLoading.postValue(false)
//            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}