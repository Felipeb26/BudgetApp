package com.batsworks.budget.ui.views

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.batsworks.budget.components.CustomOutlineTextField
import com.batsworks.budget.components.CustomText
import com.batsworks.budget.ui.theme.Color600
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.paddingScreen
import com.batsworks.budget.ui.theme.textColor

@Composable
fun SignUp(navController: NavHostController) {
	val (checked, setChecked) = remember { mutableStateOf(false) }
	val context = LocalContext.current

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.background(customBackground)
			.padding(paddingScreen())
	) {
		CustomText(text = "Cadastrar", textStyle = MaterialTheme.typography.headlineMedium)
		Spacer(modifier = Modifier.height(20.dp))
		Content()
		Row(
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Checkbox(
				checked = checked,
				onCheckedChange = { setChecked(it) },
				colors = CheckboxDefaults.colors(
					checkmarkColor = Color800,
					checkedColor = Color600
				)
			)
			ClickableText(
				text = annotedString(
					"Eu concordo com", " e ",
					"a privacidade", "a politica"
				)
			) {
				setChecked(!checked)
				Toast.makeText(context, "notificao", Toast.LENGTH_SHORT)
			}
		}
	}
}

//https://www.youtube.com/watch?v=OlO58LDfN14
@Composable
fun Content() {
	val (nome, setNome) = remember { mutableStateOf("") }
	val (email, setEmail) = remember { mutableStateOf("") }
	val (telefone, setTelefone) = remember { mutableStateOf("") }
	val (senha, setSenha) = remember { mutableStateOf("") }
	val (confirmarSenha, setConfirmaSenha) = remember { mutableStateOf("") }

	CustomOutlineTextField(
		onValueChange = { setNome(it) },
		defaultText = nome,
		labelText = "nome"
	)
	CustomOutlineTextField(
		onValueChange = { setEmail(it) },
		defaultText = email,
		labelText = "email"
	)
	CustomOutlineTextField(
		onValueChange = { setTelefone(it) },
		defaultText = telefone,
		labelText = "text"
	)
	CustomOutlineTextField(
		onValueChange = { setSenha(it) },
		defaultText = senha,
		labelText = "password"
	)
	CustomOutlineTextField(
		onValueChange = { setConfirmaSenha(it) },
		defaultText = confirmarSenha,
		labelText = "confirm password"
	)
}

@Composable
fun annotedString(text1: String, text2: String, vararg values: String): AnnotatedString {
	return buildAnnotatedString {
		withStyle(SpanStyle(color = textColor)) {
			append(text1)
		}
		append(" ")
		values.forEachIndexed { index, value ->
			withStyle(SpanStyle(color = textColor)) {
				if (index == 1) append(text2) else append(" ")
			}
			withStyle(SpanStyle(color = Color.Red.copy(0.7f))) {
				pushStringAnnotation(tag = value, value)
				append(value)
			}
		}
	}
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true
)
@Composable
fun SignUpDark() {
	SignUp(rememberNavController())
}

@Preview(
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true
)
@Composable
fun SignUpWhite() {
	SignUp(rememberNavController())
}