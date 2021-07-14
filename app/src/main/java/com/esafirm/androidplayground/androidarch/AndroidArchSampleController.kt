package com.esafirm.androidplayground.androidarch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.androidarch.room.RelationController
import com.esafirm.androidplayground.androidarch.room.RoomGettingStartedController
import com.esafirm.androidplayground.androidarch.room.TransactionController
import com.esafirm.androidplayground.androidarch.viewmodel.RestoreStateViewModelScreen
import com.esafirm.androidplayground.androidarch.workmanager.WorkManagerController
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.common.navigateToController

class AndroidArchSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(
            container.context, listOf(
                "Room - Getting Started" navigateToController { RoomGettingStartedController() },
                "Room - Relation" navigateToController { RelationController() },
                "Room - Transaction" navigateToController { TransactionController() },
                "Work Manager" navigateToController { WorkManagerController() },
                "View Model Save State" navigateTo {
                    RestoreStateViewModelScreen.start(
                        requiredContext
                    )
                }
            )
        )
    }
}

