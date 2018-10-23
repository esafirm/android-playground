package com.esafirm.androidplayground.androidarch.workmanager

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.matchParent
import com.esafirm.conductorextra.addLifecycleCallback
import java.time.Duration

class WorkManagerController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val context = container.context
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            matchParent()

            // Simple
            button("Simple - Start One Time") {
                startOneTimeWorker()
            }
            button("Simple - Start Periodic") {
                startPeriodicWorker()
            }

            // Listen
            button("Start listenable work") {
                startListenableWorker()
            }

            // Continuous Work
            button("Continuous Work") {
                startContinuousWorker()
            }

            addView(Logger.getLogView(context))
        }
    }

    private fun startContinuousWorker() {
        Logger.log("Start continuous work")
        WorkManager.getInstance()
                .beginWith(OneTimeWorkRequestBuilder<ComputeWorker>().build())
                .then(OneTimeWorkRequestBuilder<ComputeWorker>().build())
                .enqueue()
    }

    private fun startListenableWorker() {
        val tag = "WorkTag"
        val workRequest = OneTimeWorkRequestBuilder<ComputeWorker>()
                .addTag(tag)
                .build()

        val workId = workRequest.id
        val liveData = WorkManager.getInstance().getStatusByIdLiveData(workId)

        val observer = { status: WorkStatus ->
            Logger.log("Work completed: $status")
        }

        liveData.observeForever(observer)

        addLifecycleCallback(onPreDestroyView = { _, _, remover ->
            liveData.removeObserver(observer)
            remover()
        })

        WorkManager.getInstance().enqueue(workRequest)
    }

    private fun startOneTimeWorker() {
        WorkManager.getInstance().enqueue(OneTimeWorkRequestBuilder<ComputeWorker>().build())
    }

    private fun startPeriodicWorker() {
        if (Build.VERSION.SDK_INT >= 26) {
            WorkManager.getInstance().enqueue(
                    PeriodicWorkRequestBuilder<ComputeWorker>(repeatInterval = Duration.ofSeconds(5)).build())
        } else {
            Toast.makeText(activity, "SDK >= 26 only", Toast.LENGTH_SHORT).show()
        }
    }

}
