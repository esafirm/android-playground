package com.esafirm.androidplayground.androidarch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.androidarch.room.RelationController
import com.esafirm.androidplayground.androidarch.room.RoomGettingStartedController
import com.esafirm.androidplayground.androidarch.room.TransactionController
import com.esafirm.androidplayground.androidarch.workmanager.WorkManagerController
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.utils.routerStart

class AndroidArchSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
                "Room - Getting Started",
                "Room - Relation",
                "Room - Transaction",
                "Work Manager"
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = activity?.run {
        when (index) {
            0 -> routerStart(ControllerMaker { RoomGettingStartedController() })
            1 -> routerStart(ControllerMaker { RelationController() })
            2 -> routerStart(ControllerMaker { TransactionController() })
            3 -> routerStart(ControllerMaker { WorkManagerController() })
        }
    }
}

