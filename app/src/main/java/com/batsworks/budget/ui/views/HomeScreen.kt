package com.batsworks.budget.ui.views

import android.content.res.Configuration
import androidx.compose.animation.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.AnimateIcon
import com.batsworks.budget.ui.theme.Color200
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color700
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customDarkBackground

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

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
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
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(4) {
                    if (it == 1) {
                        CustomText(
                            modifier = Modifier
                                .clickable {
                                    easyNavigate(
                                        navController,
                                        Screen.HistoricoScreen.route
                                    )
                                },
                            textAlign = TextAlign.End,
                            text = "Historico",
                            textDecoration = TextDecoration.Underline,
                            textWeight = FontWeight.Bold
                        )
                    } else if (it == 2) {
                        CustomText(
                            modifier = Modifier.weight(1f),
                            text = "Ultimas entradas e saídas",
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LimitedHistory()
        }
    }
}

@Composable
fun LimitedHistory() {
    val (makeShine, setMakeShine) = remember { mutableStateOf(false) }
    val (blinkBorder, setBlinkBorder) = remember { mutableStateOf(true) }
    val borderColor = remember { Animatable(Color200) }


    LazyColumn(content = {
        items(6, itemContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 10.dp)
                    .border(2.dp, borderColor.value, RoundedCornerShape(25))
                    .padding(all = 10.dp),
            ) {
                val color = remember {
                    Animatable(
                        if ((it % 2) == 0) Color.Green.copy(0.6f) else Color.Red.copy(0.6f)
                    )
                }
                AnimateIcon(makeShine, setMakeShine, color)
                AnimateIcon(blinkBorder, setBlinkBorder, borderColor, 0.8f)


                Image(
                    painter = painterResource(if ((it % 2) == 0) R.drawable.icons8_arrowup else R.drawable.icons8_arrowdown),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = color.value)
                )
                Spacer(modifier = Modifier.width(10.dp))
                CustomText(text = if ((it % 2) == 0) "Entrada $it" else "saida $it")
                Spacer(modifier = Modifier.weight(1f))
                CustomText(text = "Valor de R$ 5$it")
            }
        })
    })
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