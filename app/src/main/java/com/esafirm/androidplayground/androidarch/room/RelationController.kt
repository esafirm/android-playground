package com.esafirm.androidplayground.androidarch.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.androidarch.room.database.AppDatabase
import com.esafirm.androidplayground.androidarch.room.database.Car
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class RelationController : BaseController() {

    private val database by lazy { AppDatabase.get(applicationContext!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            Logger.getLogView(container.context)

    override fun onAttach(view: View) {
        super.onAttach(view)
        insertData().subscribeOn(Schedulers.io()).subscribe()
        getUserWithCars().subscribeOn(Schedulers.io()).subscribe()
    }

    private fun insertData() = Completable.fromAction {
        val userDao = database.userDao()
        userDao.getUsers().firstOrNull()?.let { user ->

            val userId = user.userId ?: throw IllegalStateException("User ID can't be empty at this point")
            val cars = listOf(
                    Car(name = "Yaris", owner = userId),
                    Car(name = "Jazz", owner = userId),
                    Car(name = "Ayla", owner = userId),
                    Car(name = "Avanza", owner = userId)
            )

            database.carDao()

            val carDao = database.carDao()
            cars.forEach {
                carDao.insertCar(it)
                Logger.log("Inserting car $it")
            }
        }
    }

    private fun getUserWithCars() = Completable.fromAction {
        Logger.log("=========")

        val userDao = database.userDao()
        userDao.getUserWithCars()
                .filter { it.cars?.isEmpty()?.not() ?: false }
                .forEach {
                    Logger.log("User $it")
                }
    }
}
