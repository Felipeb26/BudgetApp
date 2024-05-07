package com.batsworks.budget.ui.views

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.customDarkBackground

@Composable
fun Add(navController: NavController) {
	val configuration = LocalConfiguration.current
	val (showPreview, setShowPreview) = remember { mutableStateOf(false) }
	val (file, setFile) = remember { mutableStateOf<Uri?>(null) }

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(customDarkBackground)
	) {
		item { Content() }
		item { ActionButtons(file, setFile, showPreview, setShowPreview) }
		item {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(0.dp)
					.padding(vertical = 20.dp, horizontal = 10.dp),
				horizontalArrangement = Arrangement.Center
			) {
				if (showPreview) {
					AsyncImage(
						modifier = Modifier
							.border(2.dp, color = Color500, RoundedCornerShape(5))
							.width((configuration.screenWidthDp / 1.5).dp)
							.height((configuration.screenHeightDp / 2).dp),
						model = file,
						contentDescription = "",
						contentScale = ContentScale.Crop
					)
					Spacer(modifier = Modifier.width(10.dp))
				}
				if (file != null) CustomButton(
					modifier = Modifier.weight(1f),
					onClick = { /*TODO*/ },
					text = "Salvar"
				)
			}
		}
	}
}

@Composable
fun Content() {
	Spacer(modifier = Modifier.height(10.dp))
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(0.dp)
			.padding(horizontal = 15.dp),
		horizontalAlignment = Alignment.Start
	) {
		CustomOutlineTextField(onValueChange = {}, labelText = "Nome da despesa")
		CustomOutlineTextField(onValueChange = {}, labelText = "Valor da despesa")
	}
}

@Composable
fun ActionButtons(
	file: Uri? = null,
	setFile: (Uri?) -> Unit,
	showPreview: Boolean,
	setShowPreview: (Boolean) -> Unit,
) {
	val selectFile = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent(),
		onResult = { uri -> setFile(uri) }
	)

	val selectImage = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.PickVisualMedia(),
		onResult = { uri -> setFile(uri) }
	)
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceAround
	) {
		CustomButton(
			onClick = { selectFile.launch("application/pdf") },
			enable = true,
			text = "select file",
			textStyle = MaterialTheme.typography.labelMedium
		)
		CustomButton(
			onClick = { selectImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
			enable = true,
			text = "select image",
			textStyle = MaterialTheme.typography.labelMedium
		)
		CustomButton(
			textStyle = MaterialTheme.typography.labelMedium,
			onClick = { setShowPreview(!showPreview) },
			text = if (showPreview) "hide file" else "show file",
			enable = file != null
		)
	}
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true
)
@Composable
fun AddWhite() {
	Add(rememberNavController())
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true
)
@Composable
fun AddDark() {
	Add(rememberNavController())
}