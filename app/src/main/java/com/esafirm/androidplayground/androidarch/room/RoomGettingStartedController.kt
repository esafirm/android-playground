package com.esafirm.androidplayground.androidarch.room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.esafirm.androidplayground.androidarch.room.database.AppDatabase
import com.esafirm.androidplayground.androidarch.room.database.User
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.column
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.matchParent
import com.esafirm.androidplayground.utils.row
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomGettingStartedController : BaseController() {

    private val database: AppDatabase by lazy { AppDatabase.get(requiredContext) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            addView(column {
                button("Insert") {
                    inserUsers().subscribeOn(Schedulers.io()).subscribe()
                }
                button("Read") {
                    getUsersWithAge().subscribeOn(Schedulers.io()).subscribe()
                }
                button("Delete") {
                    deleteOldestUser().subscribeOn(Schedulers.io()).subscribe()
                }
                button("Update") {
                    update().subscribeOn(Schedulers.io()).subscribe()
                }
            }.also { matchParent(horizontal = true, vertical = false) })
            logger()
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        init()
    }

    @SuppressLint("CheckResult")
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
