package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.navigation.easyNavigate
import com.batsworks.budget.ui.objects.HomeCard
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.InfiniteBlink
import com.batsworks.budget.ui.theme.customDarkBackground
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Home(navController: NavController) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(customDarkBackground)
	) {
		val configuration = LocalConfiguration.current
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
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(25.dp, 30.dp, 25.dp, 15.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				CustomText(
					text = "Hello catatau!",
					color = Color50,
					textStyle = MaterialTheme.typography.titleMedium
				)
				CustomText(
					text = "Hello catatau!",
					color = Color50,
					textStyle = MaterialTheme.typography.titleMedium
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(25.dp, 25.dp, 25.dp, 0.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				CustomText(
					text = "Hello catatau!",
					color = Color50,
					textStyle = MaterialTheme.typography.titleMedium
				)
				CustomText(
					text = "Hello catatau!",
					color = Color50,
					textStyle = MaterialTheme.typography.titleMedium
				)
			}
		}
		Spacer(modifier = Modifier.height(35.dp))
		Cards(navController)
		LimitedHistory()
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
fun LimitedHistory() {
	val dark = isSystemInDarkTheme()
	val (isVisible, setVisible) = remember { mutableStateOf(false) }
	val (icon, setIcon) = remember { mutableStateOf(Icons.Rounded.KeyboardArrowUp) }

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
					.clickable { setVisible(!isVisible) },
				verticalAlignment = Alignment.CenterVertically,
			) {
				Box(
					modifier = Modifier
						.clip(CircleShape)
						.background(Color700)
						.clickable {
							setVisible(!isVisible)
							if (isVisible) setIcon(Icons.Rounded.KeyboardArrowUp) else setIcon(Icons.Rounded.KeyboardArrowDown)
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
			if (isVisible) {
				BoxWithConstraints(
					modifier = Modifier
						.fillMaxSize()
						.clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
						.background(if (isSystemInDarkTheme()) Color800 else Color600)
				) {
					val boxwithContraints = this
					val width = boxwithContraints.maxWidth / 3

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
								modifier = Modifier
									.width(width)
									.padding(horizontal = 15.dp),
								textWeight = FontWeight.Bold,
								textAlign = TextAlign.Center,
								isUpperCase = true,
								text = "criado em"
							)
						}
						Spacer(modifier = Modifier.height(16.dp))
						LazyColumn {
							items(6) {
								val color = InfiniteBlink(
									if ((it % 2) == 0) Color.Green.copy(0.6f)
									else Color.Red.copy(0.6f)
								)
								CurrencyItem(
									it,
									width,
									color
								)
							}
						}
					}
				}
			}
		}
	}
}

@Composable
fun CurrencyItem(index: Int, width: Dp, color: Color) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box(
			modifier = Modifier
				.clip(RoundedCornerShape(5.dp))
				.background(color.copy(0.3f))
		) {
			Image(
				painter = painterResource(if ((index % 2) == 0) R.drawable.icons8_arrowup else R.drawable.icons8_arrowdown),
				contentDescription = "",
				colorFilter = ColorFilter.tint(color = color)
			)
		}
		Spacer(modifier = Modifier.width(5.dp))

		CustomText(
			modifier = Modifier.width(width),
			text = if ((index % 2) == 0) "Entrada $index" else "saida $index",
			color = Color50
		)
		CustomText(
			modifier = Modifier.width((width.value).dp),
			text = "R$ 5$index",
			color = Color50
		)
		CustomText(
			modifier = Modifier.width(width),
			text = LocalDateTime.now().format(
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
	Home(rememberNavController())
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showSystemUi = true
)
@Composable
fun HomeDark() {
	Home(rememberNavController())
}