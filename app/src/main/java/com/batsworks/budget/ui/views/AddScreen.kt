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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.CustomToast
import com.batsworks.budget.components.Loading
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.getByteArrayFromUri
import com.batsworks.budget.ui.view_model.add.AddViewModel
import com.batsworks.budget.ui.view_model.add.AmountFormEvent
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customDarkBackground
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun Add(model: AddViewModel = viewModel<AddViewModel>()) {
    val configuration = LocalConfiguration.current
    val (showPreview, setShowPreview) = remember { mutableStateOf(false) }
    val (file, setFile) = remember { mutableStateOf<Uri?>(null) }
    val loading = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(file) {
        delay(500)
        if (file != null) {
            val bytes = getByteArrayFromUri(context, file)
            model.onEvent(AmountFormEvent.FileVoucher(bytes))
        }
    }
    LaunchedEffect(context) {
        model.resourceEventFlow.collect { event ->
            when (event) {
                is Resource.Loading -> {
                    CustomToast(context, "carregando")
                    loading.value = event.loading
                }

                is Resource.Failure -> CustomToast(
                    context,
                    event.error ?: "Não doi possivel adicionar conta"
                )

                is Resource.Sucess -> {
                    CustomToast(context, "conta cadastrada com sucesso")
                }
            }
        }
    }

    if (!loading.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(customDarkBackground)
        ) {
            item { AddContent(model) }
            item { ActionButtons(file, setFile, showPreview, setShowPreview, model) }
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
                    CustomButton(
                        modifier = Modifier.weight(1f),
                        enable = true,
                        onClick = {
                            model.onEvent(AmountFormEvent.Submit)
                            setFile(null)
                        },
                        text = "Salvar"
                    )
                }
            }
        }
    }
    Loading(isLoading = loading.value)
}

@Composable
fun AddContent(model: AddViewModel) {
    val exchangeTypes = arrayOf("entrance", "exit")
    val (expense, setExpense) = remember { mutableStateOf("") }
    val (valueExpense, setValueExpense) = remember { mutableStateOf("") }
    val entrance = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        CustomOutlineTextField(
            modifier = Modifier.fillMaxWidth(0.95f),
            labelText = "Nome da despesa",
            defaultText = expense,
            onValueChange = {
                setExpense(it)
                model.onEvent(AmountFormEvent.ChargeNameEventChange(it))
            }, error = model.state.chargeNameError != null,
            errorMessage = model.state.chargeNameError
        )
        CustomOutlineTextField(
            modifier = Modifier.fillMaxWidth(0.95f),
            labelText = "Valor da despesa",
            defaultText = valueExpense,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                setValueExpense(it)
                if (it.isNotBlank()) model.onEvent(AmountFormEvent.ValueEventChange(BigDecimal(it)))
            }, error = model.state.valueError != null,
            errorMessage = model.state.valueError
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            exchangeTypes.forEachIndexed { index, exchange ->
                if ((index % 2) != 0) {
                    EntranceButton(Modifier.weight(1f), exchange, !entrance.value, entrance, model)
                } else {
                    EntranceButton(Modifier.weight(1f), exchange, entrance.value, entrance, model)
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    file: Uri? = null,
    setFile: (Uri?) -> Unit,
    showPreview: Boolean,
    setShowPreview: (Boolean) -> Unit,
    model: AddViewModel
) {
    val verifyFile = remember { mutableStateOf(false) }

    val selectImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> setFile(uri) }
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (model.state.fileError != null) CustomText(text = model.state.fileError ?: "")
        CustomButton(
            onClick = {
                selectImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                verifyFile.value != !verifyFile.value
            },
            enable = true,
            text = "select image",
            textStyle = MaterialTheme.typography.labelMedium
        )
        CustomButton(
            onClick = {
                setFile(null)
                setShowPreview(false)
            },
            enable = true,
            text = "remove file",
            textStyle = MaterialTheme.typography.labelSmall
        )
        CustomButton(
            textStyle = MaterialTheme.typography.labelSmall,
            text = if (file == null) "hide file" else "show file",
            onClick = {
                setShowPreview.invoke(!showPreview)
            },
            enable = file != null
        )
        CalendarPick()
    }
}

@Composable
fun CalendarPick(){
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.NOON) }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialog = rememberMaterialDialogState()

    CustomButton(onClick = { dateDialogState.show() }, enable = true, text = "teste calendar")
}

@Composable
fun EntranceButton(
    modifier: Modifier,
    exchange: String,
    entrance: Boolean,
    mutableEntrance: MutableState<Boolean>,
    model: AddViewModel
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(colors = CheckboxDefaults.colors(
            checkmarkColor = Color800,
            checkedColor = Color600
        ),
            checked = entrance,
            onCheckedChange = {
                mutableEntrance.value = !mutableEntrance.value
                model.onEvent(AmountFormEvent.EntranceEventChange(mutableEntrance.value))
            })
        CustomText(text = exchange, isUpperCase = true, textWeight = FontWeight.Bold)
    }
    CustomText(text = model.state.entranceError ?: "")
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun AddWhite() {
    Add()
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun AddDark() {
    Add()
}