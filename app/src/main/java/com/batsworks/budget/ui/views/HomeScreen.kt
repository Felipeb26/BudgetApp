package com.batsworks.budget.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.formatScreenTitle
import com.batsworks.budget.components.formatter
import com.batsworks.budget.components.functions.composeBool
import com.batsworks.budget.components.functions.notEnableIfEmpty
import com.batsworks.budget.components.visibilityIsOn
import com.batsworks.budget.domain.dto.AmountState
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.isEntrance
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.objects.HomeCard
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.Color950
import com.batsworks.budget.ui.theme.SwitchElementsView
import com.batsworks.budget.ui.theme.brushCard
import com.batsworks.budget.ui.theme.brushIcon
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.textColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

@Composable
fun Home(
    navController: NavHostController,
    amounts: StateFlow<List<AmountEntity>>,
    amountStateFlow: StateFlow<AmountState?>,
    showAmount: (BigDecimal?, Boolean, MutableState<String>) -> String
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackground)
            .drawBehind {
                drawRect(
                    brush = brushCard(cardEnd = (configuration.screenHeightDp / 2).toFloat()),
                    size = Size(
                        width = configuration.screenWidthDp.toFloat() * 5,
                        height = (configuration.screenHeightDp / 2).toFloat()
                    )
                )
            }
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        ProfileLowInfo(amountStateFlow, showAmount)
        Spacer(modifier = Modifier.height(35.dp))
        Cards(navController)
        LimitedHistory(amounts)
    }
}


@Composable
fun ProfileLowInfo(
    amountStateFlow: StateFlow<AmountState?>,
    showAmount: (BigDecimal?, Boolean, MutableState<String>) -> String,
) {
    val amountsState = amountStateFlow.collectAsState()
    val emptyValue: String = stringResource(id = R.string.empty_value)
    val showValues = remember { mutableStateOf(false) }

    val current = remember { mutableStateOf(emptyValue) }
    val future = remember { mutableStateOf(emptyValue) }
    val billing = remember { mutableStateOf(emptyValue) }
    val charge = remember { mutableStateOf(emptyValue) }
    var textAlign by remember { mutableStateOf(TextAlign.Start) }

    val title = MaterialTheme.typography.titleMedium
    val bold = FontWeight.Bold

    LaunchedEffect(current.value) {
        textAlign = if (current.value == emptyValue) TextAlign.Center else TextAlign.Start
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(15.dp, 0.dp),
        border = BorderStroke(2.dp, Color600),
        colors = CardDefaults.cardColors(
            containerColor = Color600.copy(0.4f),
            contentColor = Color50
        )
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                CustomText(
                    capitalize = true, textStyle = title,
                    text = stringResource(id = R.string.current_balance),
                    textWeight = bold, color = Color50
                )
            }
            item {}
            item {
                CustomText(
                    textStyle = title, text = stringResource(id = R.string.show), color = Color50,
                    isUpperCase = true, textWeight = bold,
                    iconBitMap = visibilityIsOn(!showValues.value),
                    clickEvent = { showValues.value = !showValues.value }
                )
            }
            item {
                ValueView(showValues.value) {
                    amountsState.value?.current.let {
                        showAmount(it, showValues.value, current)
                    }
                }
            }
            for (i in 1..5) {
                item { Spacer(modifier = Modifier.height(25.dp)) }
            }
            item {
                CustomText(
                    capitalize = true, textStyle = title,
                    text = stringResource(id = R.string.future_balance),
                    textWeight = bold, color = Color50
                )
            }
            item {
                CustomText(
                    capitalize = true, textStyle = title,
                    text = stringResource(id = R.string.future_credit),
                    textWeight = bold, color = Color50
                )
            }
            item {
                CustomText(
                    capitalize = true, textStyle = title,
                    text = stringResource(id = R.string.future_outflow),
                    textWeight = bold, color = Color50,
                )
            }
            item {
                ValueView(showValues.value) {
                    amountsState.value?.future.let {
                        showAmount(it, showValues.value, future)
                    }
                }
            }
            item {
                ValueView(showValues.value) {
                    amountsState.value?.billing.let {
                        showAmount(it, showValues.value, billing)
                    }
                }
            }
            item {
                ValueView(showValues.value) {
                    amountsState.value?.charge.let {
                        showAmount(it, showValues.value, charge)
                    }
                }
            }
        }
    }
}


@Composable
private fun ValueView(
    showValue: Boolean,
    showAmount: () -> String
) {
    val title = MaterialTheme.typography.titleMedium

    SwitchElementsView(
        start = showValue,
        content = {
            CustomText(
                capitalize = true, textStyle = title,
                textWeight = FontWeight.Bold, color = Color50,
                textAlign = TextAlign.Start,
                text = showAmount()
            )
        }, alternativeContent = {
            CustomText(
                capitalize = true, textStyle = title,
                textWeight = FontWeight.Bold, color = Color50,
                textAlign = TextAlign.Center,
                text = showAmount()
            )
        })
}


@Composable
fun Cards(navController: NavController) {
    val cards = arrayOf(HomeCard.Emprestimo, HomeCard.Cartoes, HomeCard.Investimentos)
    val remeberState = rememberScrollState()
    val isSystemDark = isSystemInDarkTheme()

    Row(
        modifier = Modifier.horizontalScroll(remeberState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        cards.forEachIndexed { _, card ->
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .height(130.dp)
                    .padding(horizontal = 10.dp)
                    .border(2.dp, color = card.color, RoundedCornerShape(10)),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) {
                        card.color.copy(0.75f)
                    } else card.color.copy(0.5f)
                ), onClick = { easyNavigate(navController, card.screen.route) }
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    modifier = Modifier
                        .padding(0.dp)
                        .padding(horizontal = 10.dp)
                        .drawBehind { drawRect(brush = brushIcon(isSystemDark)) },
                    imageVector = ImageVector.vectorResource(id = card.resource),
                    contentDescription = card.name,
                    tint = textColor
                )
                CustomText(
                    modifier = Modifier
                        .padding(0.dp)
                        .padding(10.dp),
                    text = formatScreenTitle(card.name),
                    textWeight = FontWeight.Bold,
                    color = Color950,
                    textStyle = TextStyle(
                        letterSpacing = TextUnit(0.7f, TextUnitType.Sp),
//                        shadow = Shadow(
//                            color = textColor, offset = Offset(0f, 2f), blurRadius = 1f
//                        )
                    )
                )
            }
        }
    }
}

@Composable
fun LimitedHistory(lastAmounts: StateFlow<List<AmountEntity>>) {
    val amounts by lastAmounts.collectAsState()
    val (isVisible, setVisible) = remember { mutableStateOf(false) }
    val (icon, setIcon) = remember { mutableStateOf(Icons.Rounded.KeyboardArrowUp) }
    val context = LocalContext.current
    val emptyTransaction = stringResource(id = R.string.empty_transaction)

    if (amounts.isEmpty()) setVisible(false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp), contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(composeBool(isSystemInDarkTheme(), Color800, Color700))
                .animateContentSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize()
                    .fillMaxWidth()
                    .clickable {
                        notEnableIfEmpty(context, emptyTransaction, amounts) {
                            setVisible(!isVisible)
                            if (isVisible) setIcon(Icons.Rounded.KeyboardArrowUp) else setIcon(
                                Icons.Rounded.KeyboardArrowDown
                            )
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color700)
                        .clickable {
                            notEnableIfEmpty(context, emptyTransaction, amounts.size) {
                                setVisible(!isVisible)
                                if (isVisible) setIcon(Icons.Rounded.KeyboardArrowUp) else setIcon(
                                    Icons.Rounded.KeyboardArrowDown
                                )
                            }
                        }
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = icon,
                        contentDescription = "",
                        tint = Color50
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                CustomText(
                    modifier = Modifier,
                    text = stringResource(id = R.string.last_transactions),
                    textDecoration = TextDecoration.Underline,
                    textWeight = FontWeight.Bold,
                    color = Color50
                )
            }
            AnimatedVisibility(
                visible = isVisible,
                enter = expandVertically(
                    animationSpec = tween(500),
                    expandFrom = Alignment.Top
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = tween(1500),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(1600))
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(if (isSystemInDarkTheme()) Color800 else Color600)
                ) {
                    val boxwithContraints = this
                    val width = (boxwithContraints.maxWidth.value / 3.05).dp

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            CustomText(
                                modifier = Modifier.width(width),
                                textWeight = FontWeight.Bold,
                                isUpperCase = true,
                                text = stringResource(id = R.string.entrance_exit)
                            )
                            CustomText(
                                modifier = Modifier
                                    .width(width)
                                    .padding(horizontal = 15.dp),
                                textWeight = FontWeight.Bold,
                                isUpperCase = true,
                                text = stringResource(id = R.string.values)
                            )
                            CustomText(
                                modifier = Modifier.width(width),
                                textWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                isUpperCase = true,
                                text = stringResource(id = R.string.receive_pay)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn {
                            items(items = amounts) { amount ->
                                val color = isEntrance(amount).copy(0.3f)
                                CurrencyItem(amount, width, color)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyItem(amount: AmountEntity, width: Dp, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .drawBehind { drawRect(color) }
        ) {
            Image(
                painter = painterResource(if (amount.entrance) R.drawable.icons8_arrowup else R.drawable.icons8_arrowdown),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = isEntrance(amount))
            )
        }
        Spacer(modifier = Modifier.width(5.dp))

        CustomText(
            modifier = Modifier.width(width),
            capitalize = true,
            text = amount.chargeName,
            color = Color50
        )
        CustomText(
            modifier = Modifier.width((width.value).dp),
            text = currency(amount.value),
            color = Color50
        )
        CustomText(
            modifier = Modifier.width(width),
            text = amount.amountDate.format(formatter()), color = Color50
        )
    }
}

@PreviewLightDark
@Composable
fun HomeDark() {
    val amount = MutableStateFlow(emptyList<AmountEntity>())
    val amountState = MutableStateFlow<AmountState?>(null)
    Home(rememberNavController(), amount.asStateFlow(), amountState) { _, _, _ -> "" }
}