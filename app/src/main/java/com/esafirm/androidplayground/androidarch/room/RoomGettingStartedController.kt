package com.esafirm.androidplayground.androidarch.room

import android.arch.persistence.room.Room
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.conductorextra.butterknife.BinderController
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomGettingStartedController : BinderController() {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext!!, AppDatabase::class.java, "test.db").build()
    }

    override fun getLayoutResId(): Int = R.layout.controller_room

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        bindingResult.findViewById<ViewGroup>(R.id.room_container)
                .addView(Logger.getLogView(bindingResult.context))

        buttonOf(bindingResult, R.id.room_btn_insert).setOnClickListener {
            inserUsers().subscribeOn(Schedulers.io()).subscribe()
        }

        buttonOf(bindingResult, R.id.room_btn_read).setOnClickListener {
            getUsersWithAge().subscribeOn(Schedulers.io()).subscribe()
        }

        buttonOf(bindingResult, R.id.room_btn_delete).setOnClickListener {
            deleteOldestUser().subscribeOn(Schedulers.io()).subscribe()
        }

        buttonOf(bindingResult, R.id.room_btn_update).setOnClickListener {
            update().subscribeOn(Schedulers.io()).subscribe()
        }

        init()
    }

    private fun init() {
        inserUsers()
                .subscribeOn(Schedulers.io())
                .andThen(getUsers())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { users, error ->
                    Logger.log("Getting all users: ${users.size}")
                }
    }

    private fun buttonOf(container: View, resId: Int): Button = container.findViewById(resId)

    private fun getUsers(): Single<List<User>> = Single.fromCallable {
        database.userDao().getUsers()
    }

    private fun update(): Completable = Completable.fromAction {
        val userDao = database.userDao()
        val nika = userDao.getUserWithAge(18).firstOrNull()
        if (nika != null) {
            userDao.insertUser(nika.copy(age = 22))
        }
        Logger.log("Changing nika age to 22")
    }

    private fun getUsersWithAge(): Completable = Completable.fromAction {
        val users = database.userDao().getUserWithAge(18)
        Logger.log("There are ${users.size} users with age 18")
    }

    private fun deleteOldestUser(): Completable = Completable.fromAction {
        val userDao = database.userDao()
        val nara = userDao.getUserWithAge(17).firstOrNull()
        nara?.let { userDao.deleteUsers(it) }
    }

    private fun inserUsers(): Completable = Completable.fromAction {
        Logger.log("Adding users to database")
        listOf(
                User(name = "Nara", age = 17),
                User(name = "Nika", age = 18),
                User(name = "Nana", age = 19)
        ).forEach {
            database.userDao().insertUser(it)
        }
    }
}
