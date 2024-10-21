package com.batsworks.budget.services.brodcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.batsworks.budget.services.worker.SyncData
import java.time.Duration
import java.util.UUID

class RetryWorkerNotification : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val workId = it.getParcelableExtra("worker_tag", UUID::class.java)
            if (workId != null) {
                val workRequest = PeriodicWorkRequestBuilder<SyncData>(Duration.ofMinutes(5))
                    .addTag(workId.toString())
                    .build()

                WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
                    "my_periodic_worker_name",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    workRequest
                )
                Toast.makeText(context, "Tarefa reiniciada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}