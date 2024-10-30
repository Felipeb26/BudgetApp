package com.batsworks.budget.services.protect

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import com.batsworks.budget.services.notification.NotificationToast

class ProtectionCheck(val context: Context) {

	init {
		val toatNotification = NotificationToast(context)
		if (isDebuggable()){
			toatNotification.show("não permitido debuggar o app")
			throw SecurityException("não permitido debuggar o app")
		}
		if (isRunningOnEmulator()) {
			toatNotification.show("não permitido rodar dentro de emuladores")
			throw SecurityException("não permitido rodar dentro de emuladores")
		}
	}

	private fun isDebuggable(): Boolean {
		return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
	}

	private fun isRunningOnEmulator(): Boolean {
		val isEmulator = (Build.FINGERPRINT.startsWith("generic") ||
				Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK build for") ||
				Build.MANUFACTURER.contains("Genymotion") ||
				(Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
				"google_sdk" == Build.PRODUCT)
		return isEmulator
	}


}