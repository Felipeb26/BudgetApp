package com.batsworks.budget.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "worker_log")
data class WorkerLogRegistry(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val workerName: String,
	val executionTime: LocalDateTime,
	val inclusionDateTime: LocalDateTime = LocalDateTime.now()
)