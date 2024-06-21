package com.batsworks.budget.ui.views

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomButton
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.Resource
import com.batsworks.budget.components.fields.CustomOutlineTextField
import com.batsworks.budget.components.getByteArrayFromUri
import com.batsworks.budget.components.localDate
import com.batsworks.budget.components.notification.NotificationToast
import com.batsworks.budget.components.visual_transformation.CurrencyTransformation
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Loading
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.view_model.add.AmountFormEvent
import com.batsworks.budget.ui.view_model.add.AmountFormState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate

@Composable
fun Add(
    resourceEventFlow: Flow<Resource<Any>>,
    onEvent: (AmountFormEvent) -> Unit,
    state: AmountFormState
) {
    val (showPreview, setShowPreview) = remember { mutableStateOf(false) }
    val (file, setFile) = remember { mutableStateOf<Uri?>(null) }
    val loading = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val toast = NotificationToast(context)

    LaunchedEffect(file) {
        delay(500)
        if (file != null) {
            val bytes = getByteArrayFromUri(context, file)
            onEvent(AmountFormEvent.FileVoucher(bytes))
        }
    }

    LaunchedEffect(context) {
        resourceEventFlow.collect { event ->
            when (event) {
                is Resource.Loading -> {
                    loading.value = event.loading
                    toast.show(context.getString(R.string.loading))
                }

                is Resource.Failure -> {
                    loading.value = !loading.value
                    toast.show(event.error ?: context.getString(R.string.adding_bill_error))
                }

                is Resource.Sucess -> {
                    loading.value = false
                    toast.show(context.getString(R.string.adding_bill_sucess))
                }
            }
        }
    }

    if (!loading.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(customBackground)
        ) {
            item { AddContent(onEvent, state) }
            item { ActionButtons(file, setFile, showPreview, setShowPreview, state) }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .padding(vertical = 20.dp, horizontal = 10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        modifier = Modifier.fillMaxWidth(),
                        enable = true,
                        onClick = {
                            onEvent(AmountFormEvent.Submit)
                            setFile(null)
                        }, text = stringResource(id = R.string.save)
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showPreview) AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .border(2.dp, color = Color500, RoundedCornerShape(5)),
                        model = file, contentDescription = "",
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
    Loading(isLoading = loading.value)
}

@Composable
fun AddContent(onEvent: (AmountFormEvent) -> Unit, state: AmountFormState) {
    val exchangeTypes = arrayOf("entrance", "output")
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
            text = expense,
            onValueChange = {
                setExpense(it)
                onEvent(AmountFormEvent.ChargeNameEventChange(it))
            }, error = state.chargeNameError != null,
            errorMessage = state.chargeNameError
        )
        CustomOutlineTextField(
            modifier = Modifier.fillMaxWidth(0.95f),
            transformation = CurrencyTransformation(),
            labelText = "Valor da despesa",
            text = valueExpense,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                setValueExpense(it)
                if (it.isNotBlank()) onEvent(AmountFormEvent.ValueEventChange(it))
            }, error = state.valueError != null,
            errorMessage = state.valueError
        )
        CalendarPick(onEvent, state)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            exchangeTypes.forEachIndexed { index, exchange ->
                if ((index % 2) != 0) {
                    EntranceButton(
                        Modifier.weight(1f),
                        exchange,
                        !entrance.value,
                        entrance,
                        onEvent,
                        state
                    )
                } else {
                    EntranceButton(
                        Modifier.weight(1f),
                        exchange,
                        entrance.value,
                        entrance,
                        onEvent,
                        state
                    )
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
    state: AmountFormState
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
        if (state.fileError != null) CustomText(text = state.fileError)
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
    }
}

@Composable
fun CalendarPick(onEvent: (AmountFormEvent) -> Unit, state: AmountFormState) {
    val context = LocalContext.current
    val toast = NotificationToast(context)
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember { derivedStateOf { localDate(date = pickedDate) } }
    val dateDialogState = rememberMaterialDialogState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomOutlineTextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            enabled = true, text = formattedDate,
            onValueChange = {},
            error = state.amountDateError != null,
            errorMessage = state.amountDateError
        )

        Spacer(modifier = Modifier.width(20.dp))

        CustomButton(
            modifier = Modifier.fillMaxSize(),
            onClick = { dateDialogState.show() },
            enable = true,
            text = "amount date"
        )
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "OK") { toast.show("selected date") }
            negativeButton(text = "not ok") { toast.show("selected date") }
        }) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "select a date",
            yearRange = LocalDate.now().year.rangeTo(LocalDate.now().year + 3),
        ) {
            onEvent(AmountFormEvent.AmountDate(localDate(date = it)))
            pickedDate = it
        }
    }
}

@Composable
fun EntranceButton(
    modifier: Modifier,
    exchange: String,
    entrance: Boolean,
    mutableEntrance: MutableState<Boolean>,
    onEvent: (AmountFormEvent) -> Unit,
    state: AmountFormState
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
                onEvent(AmountFormEvent.EntranceEventChange(mutableEntrance.value))
            })
        CustomText(text = exchange, isUpperCase = true, textWeight = FontWeight.Bold)
    }
    CustomText(text = state.entranceError ?: "")
}

@PreviewLightDark
@Composable
fun AddDark() {
    val resource = Channel<Resource<Any>>()
    Add(resource.receiveAsFlow(), { }, AmountFormState())
}