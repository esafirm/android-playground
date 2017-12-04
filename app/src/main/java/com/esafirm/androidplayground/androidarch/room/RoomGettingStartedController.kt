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

        init()
    }

    private fun init() {
        inserUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { Logger.log("Adding users to database") }
                .observeOn(Schedulers.io())
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

    private fun inserUsers(): Completable = Completable.fromAction {
        listOf(
                User(123123, "Nara", 17),
                User(111111, "Nika", 18),
                User(991199, "Nana", 19)
        ).forEach {
            database.userDao().insertUser(it)
        }
    }
}
