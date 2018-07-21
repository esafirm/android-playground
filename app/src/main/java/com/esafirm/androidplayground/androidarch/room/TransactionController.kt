package com.esafirm.androidplayground.androidarch.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import com.esafirm.androidplayground.androidarch.room.database.AppDatabase
import com.esafirm.androidplayground.androidarch.room.database.Car
import com.esafirm.androidplayground.androidarch.room.database.User
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.matchParent
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class TransactionController : BaseController() {

    private val database by lazy { AppDatabase.get(applicationContext!!) }

    // State
    private var simulateError: Boolean = false
    private var withoutTransaction: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val context = container.context
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            matchParent()

            addView(LinearLayout(context).apply {
                matchParent(vertical = false)
                orientation = LinearLayout.HORIZONTAL

                // Add button
                addView(Button(context).apply {
                    text = "Transaction 1"
                    setOnClickListener {
                        if (withoutTransaction) {
                            runWithoutTransaction()
                        } else {
                            runTransaction()
                        }
                    }
                })

                // Add button
                addView(Button(context).apply {
                    text = "Transaction 2"
                    setOnClickListener {
                        if (withoutTransaction) {
                            runSecondWithoutTransaction()
                        } else {
                            runSecondTransaction()
                        }
                    }
                })
            })

            // Add Checkbox
            addView(CheckBox(context).apply {
                matchParent(vertical = false)
                text = "Simulate error"
                setOnCheckedChangeListener { _, isChecked ->
                    simulateError = isChecked
                }
            })

            // Add Checkbox
            addView(CheckBox(context).apply {
                matchParent(vertical = false)
                text = "Run without transaction"
                setOnCheckedChangeListener { _, isChecked ->
                    withoutTransaction = isChecked
                }
            })

            addView(Logger.getLogView(context))
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        sanitize()
    }

    private fun sanitize() = runInBg {
        val userDao = database.userDao()
        val carDao = database.carDao()

        database.runInTransaction {
            carDao.deleteAll()

            val users = userDao.getUsers()
            if (users.isNotEmpty()) {
                userDao.deleteUsers(*users.toTypedArray())
            }

            listOf(User(name = "Siska", age = 20), User(name = "Saski", age = 21), User(name = "Sesko", age = 100))
                    .forEach {
                        userDao.insertUser(it)
                    }

            val userId = userDao.getUsers().first().userId!!
            carDao.insertCar(Car(name = "Brio", owner = userId))
        }
    }

    private fun runTransaction() = runInBg {
        val (oldUser, newUser) = getUsers()
        if (simulateError) {
            try {
                database.userDao().replaceUserWithError(oldUser, newUser)
            } catch (e: Exception) {
                Logger.log("Error happens!!!")
            }
        } else {
            database.userDao().replaceUser(oldUser, newUser)
        }
        printCurrentUsers()
    }

    private fun runWithoutTransaction() = runInBg {
        val userDao = database.userDao()
        val (oldUser, newUser) = getUsers()
        try {
            userDao.deleteUsers(oldUser)
            if (simulateError) throw IllegalStateException()
            userDao.insertUser(newUser)
        } catch (e: Exception) {
            Logger.log("Error happens!!!")
        }
        printCurrentUsers()
    }

    private fun getUsers(): Pair<User, User> {
        val oldUser = database.userDao().getUsers().first()
        val newUser = oldUser.copy(name = "New ${System.currentTimeMillis()}")
        return oldUser to newUser
    }

    private fun runSecondTransaction() = runInBg {
        val carDao = database.carDao()
        val userDao = database.userDao()
        try {
            database.runInTransaction {
                val user = userDao.insertUser(User(name = "Maya", age = 55))
                if (simulateError) throw IllegalStateException()
                carDao.insertCar(Car(name = "Ayla", owner = user.toInt()))
            }
        } catch (e: Exception) {
            Logger.log("Error happens!!")
        }

        printCurrentUsers()
        printCurrentCars()
    }

    private fun runSecondWithoutTransaction() = runInBg {
        val carDao = database.carDao()
        val userDao = database.userDao()
        try {
            val user = userDao.insertUser(User(name = "Maya", age = 55))
            if (simulateError) throw IllegalStateException()
            carDao.insertCar(Car(name = "Ayla", owner = user.toInt()))
        } catch (e: Exception) {
            Logger.log("Error happens!!")
        }

        printCurrentUsers()
        printCurrentCars()
    }

    private fun printCurrentCars() {
        Logger.log("Current cars:")
        database.carDao().getAllData().forEach {
            Logger.log(it.name)
        }
    }

    private fun printCurrentUsers() {
        Logger.log("Current users:")
        database.userDao().getUsers().forEach {
            Logger.log(it.name)
        }
    }

    private fun runInBg(action: () -> Unit) {
        Completable.fromAction { action() }.subscribeOn(Schedulers.io()).subscribe()
    }
}
