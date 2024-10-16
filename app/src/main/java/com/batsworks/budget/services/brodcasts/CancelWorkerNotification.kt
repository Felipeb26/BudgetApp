package com.batsworks.budget.services.brodcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.batsworks.budget.services.notification.NotificationToast
import java.util.UUID

class CancelWorkerNotification : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        val toast = context?.let { NotificationToast(it) }
        toast?.show("houver uma acao")

        intent?.let {
            val workId = it.getParcelableExtra("worker_tag", UUID::class.java)
            workId?.let {
                WorkManager.getInstance(context!!).cancelWorkById(workId)
                NotificationManagerCompat.from(context).cancel(1)
            }
        }
    }
}