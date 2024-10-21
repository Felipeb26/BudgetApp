package com.batsworks.budget.ui.view_model.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.batsworks.budget.data.dao.UsersDao
import com.batsworks.budget.data.entity.UserEntity
import com.batsworks.budget.services.worker.SyncData
import com.batsworks.budget.ui.components.menu.AJUST_TAG
import com.batsworks.budget.ui.theme.custom.THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localRepository: UsersDao,
    @ApplicationContext val context: Context
) : ViewModel() {

    val tag: String = AJUST_TAG(SettingsViewModel::class.java.name)

    var user by mutableStateOf<UserEntity?>(null)

    init {
        runBlocking {
            user = localRepository.findUser()
        }
    }

    fun saveTheme(theme: THEME) {
        viewModelScope.launch {
            localRepository.updateTheme(theme.theme)
        }
    }

    fun forceDataToSync() {
        val contraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workerRequest = OneTimeWorkRequestBuilder<SyncData>()
            .setConstraints(contraints)
            .setInitialDelay(Duration.ofSeconds(5))
            .addTag(tag)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(workerRequest)
    }
}