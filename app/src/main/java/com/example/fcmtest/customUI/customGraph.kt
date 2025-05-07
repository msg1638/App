package com.example.fcmtest.customUI

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.fcmtest.analytics.StatsResult
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount

@Composable
fun column_chart(stat: StatsResult) {
    val timeLabels = listOf("00~05시", "06~11시", "12~17시", "18~23시")
    val incidentCount = stat.timeRangeCount
    val datalist = mutableListOf<Bars>()
    timeLabels.forEachIndexed { index, label ->
        datalist.add(
            Bars(
                label = label,
                values = listOf(
                    Bars.Data(
                        label = "낙상 수",
                        value = incidentCount[index].toDouble(),
                        color = SolidColor(Color.Blue)
                    )
                )
            )
        )
    }
    ColumnChart(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        data = datalist,
        indicatorProperties = HorizontalIndicatorProperties(
            count = IndicatorCount.CountBased(count = stat.timeRangeCount.max() + 1),
            contentBuilder = { indicator ->
                "%d".format(indicator.toInt())
            },
        ),
        barProperties = BarProperties(
            thickness = 15.dp,
            spacing = 4.dp,
            cornerRadius = Bars.Data.Radius.Circular(6.dp),
            style = DrawStyle.Fill
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
    )
}