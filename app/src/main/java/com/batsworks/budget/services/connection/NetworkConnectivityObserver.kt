package com.batsworks.budget.services.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkConnectivityObserver @Inject constructor(private val connectivityManager: ConnectivityManager) : LiveData<Boolean>() {

	override fun onActive() {
		super.onActive()
		checkNetworkInfo()
	}

	override fun onInactive() {
		super.onInactive()
		connectivityManager.unregisterNetworkCallback(networkCallback)
	}


	private val networkCallback = object : ConnectivityManager.NetworkCallback() {
		override fun onAvailable(network: Network) {
			super.onAvailable(network)
			postValue(true)
		}

		override fun onUnavailable() {
			super.onUnavailable()
			postValue(false)
		}

		override fun onLost(network: Network) {
			super.onLost(network)
			postValue(false)
		}
	}

	private fun checkNetworkInfo() {
		val network = connectivityManager.activeNetwork
		if (network == null) {
			postValue(false)
		}
		val request = NetworkRequest.Builder().apply {
			addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
			addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
			addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
			addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
			addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				addCapability(NetworkCapabilities.NET_CAPABILITY_PRIORITIZE_BANDWIDTH)
				addCapability(NetworkCapabilities.NET_CAPABILITY_PRIORITIZE_LATENCY)
			}
		}.build()

		connectivityManager.registerNetworkCallback(request, networkCallback)
	}
}