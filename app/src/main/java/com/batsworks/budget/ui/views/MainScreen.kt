package com.batsworks.budget.ui.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.R
import com.batsworks.budget.navigation.Screen
import com.batsworks.budget.ui.theme.Color50
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.GradientBrush

@Composable
fun Main(controller: NavController) {
    Box(
        Modifier
            .height(160.dp)
            .fillMaxWidth()
            .background(brush = GradientBrush())
    ) {}

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
                Text(
                    text = "Hello catatau!",
                    modifier = Modifier.clickable {
                        controller.navigate(Screen.LoginScreen.route)
                    }
                )
                Text(
                    text = "Hello catatau!",
                    modifier = Modifier.clickable {
                        controller.navigate(Screen.LoginScreen.route)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp, 25.dp, 25.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hello catatau!",
                    modifier = Modifier.clickable {
                        controller.navigate(Screen.LoginScreen.route)
                    }
                )
                Text(
                    text = "Hello catatau!",
                    modifier = Modifier.clickable {
                        controller.navigate(Screen.LoginScreen.route)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            text = "Hitorico",
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = "Ultimas entradas e saídas"
        )
        LazyColumn(content = {
            items(6, itemContent = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 10.dp)
                        .border(2.dp, Color(0xffd8d5f0), RoundedCornerShape(25))
                        .padding(all = 10.dp),
                ) {
                    Image(
                        painter = painterResource(if ((it % 2) == 0) R.drawable.icons8_arrowup else R.drawable.icons8_arrowdown),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = if ((it % 2) == 0) "Entrada $it" else "saida $it")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Valor de R$ 5$it")
                }
            })
        })
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
fun MainWhite() {
    Main(controller = rememberNavController())
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
fun MainDark() {
    Main(controller = rememberNavController())
}