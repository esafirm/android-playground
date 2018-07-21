package com.esafirm.androidplayground.androidarch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.androidarch.room.RelationController
import com.esafirm.androidplayground.androidarch.room.RoomGettingStartedController
import com.esafirm.androidplayground.androidarch.room.TransactionController
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.main.RouterAct

class AndroidArchSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
                "Room - Getting Started",
                "Room - Relation",
                "Room - Transaction"
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) {
        when (index) {
            0 -> RouterAct.start(activity!!, ControllerMaker { RoomGettingStartedController() })
            1 -> RouterAct.start(activity!!, ControllerMaker { RelationController() })
            2 -> RouterAct.start(activity!!, ControllerMaker { TransactionController() })
        }
    }
}

