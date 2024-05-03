package com.batsworks.budget.ui.state

import android.util.Log
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

	private val TAG = LoginViewModel::class.simpleName;

	fun log() {
		Log.d(TAG, if (TAG.isNullOrEmpty())"teste" else TAG)
	}

}