package com.batsworks.budget.utils.files.pdf

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.batsworks.budget.R
import com.batsworks.budget.ui.components.texts.CustomText
import com.batsworks.budget.ui.theme.Color400
import java.io.ByteArrayInputStream

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComposePDFViewer(byteArray: ByteArray) {
	var isLoading by remember { mutableStateOf(false) }
	var currentLoadingPage by remember { mutableStateOf<Int?>(null) }
	var pageCount by remember { mutableStateOf<Int?>(null) }
	if (byteArray.isEmpty()) return

	Box {
		PdfViewer(
			modifier = Modifier.fillMaxSize(),
			pdfStream = ByteArrayInputStream(byteArray),
			loadingListener = { loading, currentPage, maxPage ->
				isLoading = loading
				if (currentPage != null) currentLoadingPage = currentPage
				if (maxPage != null) pageCount = maxPage
			}
		)
		if (isLoading) {
			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center
			) {
				LinearProgressIndicator(
					trackColor = Color400,
					progress = {
						if (currentLoadingPage == null || pageCount == null) 0f
						else (currentLoadingPage ?: 0).toFloat() / (pageCount ?: 0).toFloat()
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 30.dp),
				)
				CustomText(
					modifier = Modifier
						.align(Alignment.End)
						.padding(top = 5.dp)
						.padding(horizontal = 30.dp),
					text = formatPagesLoaded(currentLoadingPage, pageCount),
					textWeight = FontWeight.Bold,
					textStyle = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.Bold
					)
				)
			}
		}
	}
}

@Composable
private fun formatPagesLoaded(curentPage: Int?, pageCount: Int?): String {
	var string = stringResource(id = R.string.pdf_pages)
	string = string.replace("-", "${pageCount ?: "-"}")
	return "$curentPage $string"
}