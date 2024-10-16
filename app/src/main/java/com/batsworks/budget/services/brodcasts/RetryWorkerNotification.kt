package com.batsworks.budget.services.brodcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.batsworks.budget.services.worker.SyncData
import java.time.Duration

class RetryWorkerNotification : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val workerTag = it.getStringExtra("worker_tag")
            if (!workerTag.isNullOrEmpty()) {
                val workRequest = PeriodicWorkRequestBuilder<SyncData>(Duration.ofMinutes(5))
                    .addTag(workerTag)
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