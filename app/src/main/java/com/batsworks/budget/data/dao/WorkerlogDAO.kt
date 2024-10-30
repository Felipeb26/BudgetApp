package com.batsworks.budget.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.batsworks.budget.data.entity.WorkerLogRegistry
import java.time.LocalDate

@Dao
interface WorkerlogDAO {

	@Insert
	suspend fun insert(workerLog: WorkerLogRegistry)

	@Query("SELECT * FROM WORKER_LOG")
	suspend fun findAll(): List<WorkerLogRegistry>

	@Query("DELETE FROM WORKER_LOG WHERE id in (SELECT id FROM WORKER_LOG ORDER BY inclusionDateTime DESC LIMIT 10)")
	suspend fun deleteAll()

	@Query("SELECT COUNT(*) FROM WORKER_LOG WHERE workerName = :workerName AND DATE(executionTime) = DATE(:date)")
	suspend fun getExecutionCountForDate(workerName: String, date: LocalDate): Int
}