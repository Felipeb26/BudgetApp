package com.batsworks.budget.components.charts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.combinedchart.CombinedChart
import co.yml.charts.ui.combinedchart.model.CombinedChartData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.batsworks.budget.domain.entity.AmountEntity
import com.batsworks.budget.ui.theme.Color400
import com.batsworks.budget.ui.theme.Color800
import com.batsworks.budget.ui.theme.customBackground
import com.batsworks.budget.ui.theme.textColor
import java.math.BigDecimal

@Composable
fun CustomLineChart(
	backgroundColor: Color = customBackground,
	lineColor: Color = textColor,
) {
	val step = 5
	val pointerData = listOf(
		Point(0f, 40f),
		Point(1f, 50f),
		Point(2f, 30f),
		Point(3f, 70f),
		Point(4f, 80f),
	)

	val xAxisData = AxisData.Builder()
		.axisStepSize(100.dp)
		.backgroundColor(backgroundColor)
		.steps(pointerData.size - 1)
		.labelData { i -> i.toString() }
		.labelAndAxisLinePadding(15.dp)
		.axisLineColor(lineColor)
		.axisLabelColor(lineColor)
		.build()

	val yAxisData = AxisData.Builder()
		.axisStepSize(100.dp)
		.backgroundColor(backgroundColor)
		.steps(pointerData.size - 1)
		.labelData { i ->
			val yScale = 100 / step
			(i * yScale).toString()
		}
		.labelAndAxisLinePadding(15.dp)
		.axisLineColor(lineColor)
		.axisLabelColor(lineColor)
		.build()


	val linechart = LineChartData(
		linePlotData = LinePlotData(
			lines = listOf(
				Line(
					dataPoints = pointerData,
					LineStyle(color = Color400),
					IntersectionPoint(color = Color400),
					SelectionHighlightPoint(),
					ShadowUnderLine(color = Color800),
					SelectionHighlightPopUp()
				)
			)
		), xAxisData, yAxisData,
		gridLines = GridLines(),
		isZoomAllowed = true,
		backgroundColor = backgroundColor
	)

	LineChart(
		modifier = Modifier
			.fillMaxWidth()
			.height(300.dp),
		lineChartData = linechart
	)
}


@Composable
private fun AmountListChart(
	backgroundColor: Color = customBackground,
	lineColor: Color = textColor,
	amounts: List<AmountEntity>,
) {
	val outputValues = listOf<Point>()
	val incomingValues = listOf<Point>()

	amounts.filter { !it.entrance }.mapIndexed { index, amount ->
		outputValues.plus(Point((index - 1).toFloat(), amount.value.toFloat()))
	}
	amounts.filter { it.entrance }.mapIndexed { index, amount ->
		incomingValues.plus(Point((index - 1).toFloat(), amount.value.toFloat()))
	}

	val xAxisData = AxisData.Builder()
		.axisStepSize(100.dp)
		.backgroundColor(backgroundColor)
		.steps(amounts.size - 1)
		.labelData { i -> i.toString() }
		.labelAndAxisLinePadding(15.dp)
		.axisLineColor(lineColor)
		.axisLabelColor(lineColor)
		.build()

	val yAxisData = AxisData.Builder()
		.axisStepSize(100.dp)
		.backgroundColor(backgroundColor)
		.steps(amounts.size - 1)
		.labelData { i ->
			val yScale = 100 / (amounts.size + 3)
			(i * yScale).toString()
		}
		.labelAndAxisLinePadding(15.dp)
		.axisLineColor(lineColor)
		.axisLabelColor(lineColor)
		.build()

	val linePlotData = LinePlotData(
		lines = listOf(
			Line(
				DataUtils.getLineChartData(amounts.size, maxRange = 100),
				lineStyle = LineStyle(color = lineColor),
				intersectionPoint = IntersectionPoint(),
				selectionHighlightPoint = SelectionHighlightPoint(),
				selectionHighlightPopUp = SelectionHighlightPopUp()
			),
			Line(
				DataUtils.getLineChartData(amounts.size, maxRange = 100),
				lineStyle = LineStyle(color = lineColor.copy(0.5f)),
				intersectionPoint = IntersectionPoint(),
				selectionHighlightPoint = SelectionHighlightPoint(),
				selectionHighlightPopUp = SelectionHighlightPopUp()
			)
		)
	)

	val chartData = CombinedChartData(
		combinedPlotDataList = listOf(linePlotData),
		xAxisData = xAxisData,
		yAxisData = yAxisData
	)
	CombinedChart(
		modifier = Modifier.height(400.dp),
		combinedChartData = chartData
	)
}


@Composable
@Preview
fun PreviewLineChart() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(0.dp)
			.padding(25.dp)
	) {
		CustomLineChart()
	}
}

@Composable
@Preview
fun PreviewCombinedChart() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(0.dp)
			.padding(25.dp)
	) {
		val lists = listOf<AmountEntity>(
			AmountEntity(value = BigDecimal.valueOf(1)),
			AmountEntity(value = BigDecimal.valueOf(20)),
			AmountEntity(value = BigDecimal.valueOf(20)),
			AmountEntity(value = BigDecimal.valueOf(20)),
			AmountEntity(value = BigDecimal.valueOf(20)),
		)

		AmountListChart(amounts = lists)
	}
}