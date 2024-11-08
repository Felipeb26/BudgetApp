package com.batsworks.budget.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.batsworks.budget.ui.theme.customBackground
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

fun generateQRCode(content: String, size: Int = 512): Bitmap? {
	return try {
		val writer = QRCodeWriter()
		val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
		val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

		for (x in 0 until size) {
			for (y in 0 until size) {
				bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
			}
		}
		bitmap
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
}

@Composable
fun QRCodeImage(content: String, size: Int = 512) {
	val bitmap = generateQRCode(content, size)
	bitmap?.let {
		Image(
			modifier = Modifier.background(customBackground),
			bitmap = it.asImageBitmap(),
			contentDescription = "QR Code",
			contentScale = ContentScale.Fit
		)
	}
}