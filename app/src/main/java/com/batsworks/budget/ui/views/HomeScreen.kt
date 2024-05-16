package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.BudgetApplication
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.components.currency
import com.batsworks.budget.components.formatScreenTitle
import com.batsworks.budget.components.notEnableIfEmpty
import com.batsworks.budget.components.visibilityIsOn
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.objects.HomeCard
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customDarkBackground
import com.batsworks.budget.ui.view_model.factoryProvider
import com.batsworks.budget.ui.view_model.home.HomeViewModel
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

@Composable
fun Home(navController: NavController, model: HomeViewModel = viewModel<HomeViewModel>()) {
	val configuration = LocalConfiguration.current
	val amounts by model.lastAmounts.collectAsState()
	val profileValues = model.totalAmount.collectAsState()
	val viewValues = remember { mutableStateOf(true) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(customDarkBackground)
	) {
		Canvas(modifier = Modifier, onDraw = {
			drawRect(
				color = Color700,
				size = Size(
					configuration.screenWidthDp.toFloat() * 5,
					(configuration.screenHeightDp / 2.5).toFloat()
				)
			)
		})
		Spacer(modifier = Modifier.height(35.dp))
		ProfileLowInfo(viewValues, profileValues, model)
		Spacer(modifier = Modifier.height(35.dp))
		Cards(navController)
		LimitedHistory(amounts)
	}
}


@Composable
fun ProfileLowInfo(
	showValues: MutableState<Boolean>,
	amountsState: State<Map<String, BigDecimal>>,
	model: HomeViewModel,
) {
	val future = remember { mutableStateOf(". . .") }
	val pendencia = remember { mutableStateOf(". . .") }
	val remaining = remember { mutableStateOf(". . .") }

	val title = MaterialTheme.typography.titleMedium
	val bold = FontWeight.Bold
	val align = TextAlign.Start

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(170.dp)
			.padding(15.dp, 0.dp),
		border = BorderStroke(2.dp, Color600),
		colors = CardDefaults.cardColors(
			containerColor = Color800,
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
					text = "Saldo atual", textWeight = bold,
					color = Color50, textAlign = align
				)
			}
			item {}
			item {
				CustomText(
					textStyle = title, text = "Show", color = Color50,
					isUpperCase = true, textWeight = bold,
					iconBitMap = visibilityIsOn(!showValues.value),
					clickEvent = { showValues.value = !showValues.value }
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					textWeight = bold, color = Color50, textAlign = align,
					text = model.showAmount(
						amountsState.value["remaining"],
						showValues.value,
						remaining
					)
				)
			}
			for (i in 1..5) {
				item { Spacer(modifier = Modifier.height(25.dp)) }
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					text = "Saldo Futuro", textWeight = bold,
					color = Color50, textAlign = align
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					text = "Entrada Futura", textWeight = bold,
					color = Color50, textAlign = align
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					text = "Saida Futura", textWeight = bold,
					color = Color50, textAlign = align
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					textWeight = bold, color = Color50, textAlign = align,
					text = model.showAmount(
						amountsState.value["remaining"],
						showValues.value,
						remaining
					)
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					textWeight = bold, color = Color50, textAlign = align,
					text = model.showAmount(
						amountsState.value["entrance"],
						showValues.value,
						future
					)
				)
			}
			item {
				CustomText(
					capitalize = true, textStyle = title,
					textWeight = bold, color = Color50, textAlign = align,
					text = model.showAmount(
						amountsState.value["output"],
						showValues.value,
						pendencia
					)
				)
			}
		}
	}
}


@Composable
fun Cards(navController: NavController) {
	val cards = arrayOf(HomeCard.Emprestimo, HomeCard.Cartoes, HomeCard.Investimentos)
	val remeberState = rememberScrollState()

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
					.padding(horizontal = 10.dp),
				colors = CardDefaults.cardColors(
					containerColor = if (isSystemInDarkTheme()) {
						card.color.copy(0.6f)
					} else card.color.copy(0.8f)
				), onClick = { easyNavigate(navController, card.screen.route) }
			) {
				Spacer(modifier = Modifier.height(10.dp))
				Icon(
					modifier = Modifier
						.padding(0.dp)
						.padding(horizontal = 10.dp),
					imageVector = ImageVector.vectorResource(id = card.resource),
					contentDescription = card.name
				)
				CustomText(
					modifier = Modifier
						.padding(0.dp)
						.padding(10.dp),
					text = formatScreenTitle(card.name), textWeight = FontWeight.Bold
				)
			}
		}
	}
}

@Composable
fun LimitedHistory(amounts: List<AmountEntity>) {
	val dark = isSystemInDarkTheme()
	val (isVisible, setVisible) = remember { mutableStateOf(false) }
	val (icon, setIcon) = remember { mutableStateOf(Icons.Rounded.KeyboardArrowUp) }
	val context = LocalContext.current

	if (amounts.isEmpty()) setVisible(false)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 32.dp), contentAlignment = Alignment.BottomCenter
	) {
		Column(
			modifier = Modifier
				.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
				.background(if (dark) Color800 else Color600)
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
						notEnableIfEmpty(context, "sem saida ou entrada", amounts) {
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
							notEnableIfEmpty(context, "sem saida ou entrada", amounts.size) {
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
					text = "Ultimas entradas e saídas",
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
								text = "Entrada/Saida"
							)
							CustomText(
								modifier = Modifier
									.width(width)
									.padding(horizontal = 15.dp),
								textWeight = FontWeight.Bold,
								isUpperCase = true,
								text = "valores"
							)
							CustomText(
								modifier = Modifier.width(width),
								textWeight = FontWeight.Bold,
								textAlign = TextAlign.Start,
								isUpperCase = true,
								text = "receber/pagar"
							)
						}
						Spacer(modifier = Modifier.height(16.dp))

						LazyColumn {
							items(items = amounts) { amount ->
								val color =
									if (amount.entrance) Color.Green.copy(0.3f)
									else Color.Red.copy(0.3f)
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
fun CurrencyItem(amount: AmountEntity, width: Dp, color: Color) {
	val itemColor = if (amount.entrance) Color.Green else Color.Red

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
				colorFilter = ColorFilter.tint(color = itemColor)
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
			text = amount.amountDate.format(
				DateTimeFormatter.ofPattern("dd/MM/yyyy"),
			), color = Color50
		)
	}
}


@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showSystemUi = true
)
@Composable
fun HomeWhite() {
	val model =
		viewModel<HomeViewModel>(factory = factoryProvider(HomeViewModel(BudgetApplication.database.getAmountDao())))
	Home(rememberNavController(), model)
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showSystemUi = true
)
@Composable
fun HomeDark() {
	val model =
		viewModel<HomeViewModel>(factory = factoryProvider(HomeViewModel(BudgetApplication.database.getAmountDao())))
	Home(rememberNavController(), model)
}