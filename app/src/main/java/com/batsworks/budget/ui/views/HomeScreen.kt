package com.batsworks.budget.ui.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.animations.SwitchElementsView
import com.batsworks.budget.components.animations.Visible
import com.batsworks.budget.components.files.image.locateImage
import com.batsworks.budget.components.files.image.visibilityIsOn
import com.batsworks.budget.components.formatter.currency
import com.batsworks.budget.components.formatter.formatter
import com.batsworks.budget.components.functions.composeBool
import com.batsworks.budget.components.functions.notEnableIfEmpty
import com.batsworks.budget.components.texts.CustomText
import com.batsworks.budget.domain.dto.AmountState
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.domain.entity.isEntrance
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.services.notification.NotificationToast
import com.batsworks.budget.ui.custom_components.HomeCard
import com.batsworks.budget.ui.theme.Color300
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color500
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.ColorCardCartoes
import com.batsworks.budget.ui.theme.ColorCardEmprestimo
import com.batsworks.budget.ui.theme.ColorCardInvestimentos
import com.batsworks.budget.ui.theme.brushCard
import com.batsworks.budget.ui.theme.brushIcon
import com.batsworks.budget.ui.theme.customBackground
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

@Composable
fun Home(
	navController: NavHostController,
	amounts: StateFlow<List<AmountEntity>>,
	amountStateFlow: StateFlow<AmountState?>,
	showAmount: (BigDecimal?, Boolean, MutableState<String>) -> String,
) {
	val configuration = LocalConfiguration.current
	val height = configuration.screenHeightDp.toFloat()
	val width = configuration.screenWidthDp.toFloat()

	val imageBitMap = locateImage(
		LocalContext.current,
		R.drawable.logo, 1.2
	).asImageBitmap()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.drawBehind {
				drawRect(
					brush = brushCard(cardEnd = height / 2),
					size = Size(
						width = width * 5,
						height = height / 2
					)
				)
				drawImage(
					imageBitMap,
					alpha = 0.3f,
					topLeft = Offset(
						x = (width - imageBitMap.width) / 18,
						y = height / 2
					),
					colorFilter = ColorFilter.tint(Color400)
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
	val showValues = rememberSaveable { mutableStateOf(false) }

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
		border = BorderStroke(2.dp, Color300),
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
					textWeight = bold,
				)
			}
			item {}
			item {
				CustomText(
					textStyle = title, text = stringResource(id = R.string.show),
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
					textWeight = bold,
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					text = stringResource(id = R.string.future_credit),
					textWeight = bold,
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					text = stringResource(id = R.string.future_outflow),
					textWeight = bold,
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
	showAmount: () -> String,
) {
	val title = MaterialTheme.typography.titleMedium

	SwitchElementsView(
		start = showValue,
		content = {
			CustomText(
				capitalize = true, textStyle = title,
				textWeight = FontWeight.Bold,
				textAlign = TextAlign.Start,
				text = showAmount()
			)
		}, alternativeContent = {
			CustomText(
				capitalize = true, textStyle = title,
				textWeight = FontWeight.Bold,
				textAlign = TextAlign.Center,
				text = showAmount()
			)
		})
}


@Composable
fun Cards(navController: NavController) {
	val cards = arrayOf(HomeCard.Emprestimo, HomeCard.Cartoes, HomeCard.Investimentos)

	LazyRow(
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		items(items = cards, itemContent = { card ->
			Card(
				modifier = Modifier
					.width(150.dp)
					.height(130.dp)
					.padding(horizontal = 10.dp)
					.border(1.dp, color = Color700, RoundedCornerShape(10)),
				colors = CardDefaults.cardColors(
					containerColor = composeBool(
						isSystemInDarkTheme(),
						setCardColor(card).copy(0.8f),
						setCardColor(card).copy(0.6f)
					)
				), onClick = { easyNavigate(navController, card.screen.route) }
			) {
				Spacer(modifier = Modifier.height(10.dp))
				Icon(
					modifier = Modifier
						.padding(0.dp)
						.padding(horizontal = 10.dp)
						.drawBehind { drawRect(brush = brushIcon(true)) },
					imageVector = ImageVector.vectorResource(id = card.resource),
					contentDescription = card.getName(),
					tint = Color50
				)
				CustomText(
					modifier = Modifier
						.padding(0.dp)
						.padding(10.dp), capitalize = true,
					text = card.getName(), textWeight = FontWeight.Bold,
					color = composeBool(isSystemInDarkTheme(), Color50, Color700),
					textStyle = MaterialTheme.typography.titleMedium.copy(
						shadow = Shadow(
							color = Color700,
							offset = Offset(0f, 0f),
							blurRadius = 0.51f
						))
				)
			}
		})
	}
}

@Composable
private fun LimitedHistory(lastAmounts: StateFlow<List<AmountEntity>>) {
	val amounts by lastAmounts.collectAsState()
	var isVisible by remember { mutableStateOf(false) }
	val (icon, setIcon) = remember { mutableStateOf(Icons.Rounded.KeyboardArrowDown) }
	val context = LocalContext.current
	val emptyTransaction = stringResource(id = R.string.empty_transaction)
	val toast = NotificationToast(context)

	if (amounts.isEmpty()) isVisible = false
	LaunchedEffect(amounts.size) {
		if (amounts.isNotEmpty() || !isVisible) {
			if (!isVisible) setIcon(Icons.Rounded.KeyboardArrowUp)
		} else setIcon(Icons.Rounded.KeyboardArrowDown)
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 32.dp), contentAlignment = Alignment.BottomCenter
	) {
		Column(
			modifier = Modifier
				.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
				.background(Color400)
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
							isVisible = !isVisible
							if (isVisible) setIcon(Icons.Rounded.KeyboardArrowUp)
							else setIcon(Icons.Rounded.KeyboardArrowDown)
						}
					},
				verticalAlignment = Alignment.CenterVertically,
			) {
				Box(Modifier.clip(CircleShape).background(Color700)) {
					Icon(
						modifier = Modifier.clickable {
							if (amounts.isNotEmpty()) {
								isVisible = !isVisible
								if (isVisible) setIcon(Icons.Rounded.KeyboardArrowUp)
								else setIcon(Icons.Rounded.KeyboardArrowDown)
							} else toast.show(emptyTransaction)
						},
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
					textWeight = FontWeight.Bold, color = Color.White,
					textStyle = MaterialTheme.typography.labelLarge
				)
			}
			Visible(show = isVisible) {
				BoxWithConstraints(
					modifier = Modifier
						.fillMaxSize()
						.background(Color500.copy(0.7f))
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
								upperCase = true,
								text = stringResource(id = R.string.entrance_exit)
							)
							CustomText(
								modifier = Modifier
									.width(width)
									.padding(horizontal = 15.dp),
								textWeight = FontWeight.Bold,
								upperCase = true,
								text = stringResource(id = R.string.values)
							)
							CustomText(
								modifier = Modifier.width(width),
								textWeight = FontWeight.Bold,
								textAlign = TextAlign.Start,
								upperCase = true,
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
			capitalize = true, textWeight = FontWeight.Bold,
			text = amount.chargeName,
		)
		CustomText(
			modifier = Modifier.width((width.value).dp),
			text = currency(amount.value),
			textWeight = FontWeight.Bold
		)
		CustomText(
			modifier = Modifier.width(width),
			text = amount.amountDate.format(formatter()),
			textWeight = FontWeight.Bold
		)
	}
}

@Composable
private fun setCardColor(card: HomeCard): Color {
	return when (card) {
		HomeCard.Cartoes -> ColorCardCartoes
		HomeCard.Emprestimo -> ColorCardEmprestimo
		HomeCard.Investimentos -> ColorCardInvestimentos
	}
}

@PreviewLightDark
@Composable
fun HomeDark() {
	val amount = MutableStateFlow(emptyList<AmountEntity>())
	val amountState = MutableStateFlow<AmountState?>(null)
	Home(rememberNavController(), amount.asStateFlow(), amountState) { _, _, _ -> "" }
}