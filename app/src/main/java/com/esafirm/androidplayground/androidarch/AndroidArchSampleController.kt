package com.esafirm.androidplayground.androidarch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.androidarch.room.RelationController
import com.esafirm.androidplayground.androidarch.room.RoomGettingStartedController
import com.esafirm.androidplayground.androidarch.room.TransactionController
import com.esafirm.androidplayground.androidarch.workmanager.WorkManagerController
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class AndroidArchSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
                "Room - Getting Started",
                "Room - Relation",
                "Room - Transaction",
                "Work Manager"
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = start {
        when (index) {
            0 -> RoomGettingStartedController()
            1 -> RelationController()
            2 -> TransactionController()
            3 -> WorkManagerController()
            else -> throw IllegalStateException("Undefined index!")
        }
    }
}

