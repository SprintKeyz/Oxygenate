package com.sprintkeyz.oxygenate.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.sprintkeyz.oxygenate.data.ConfigItem
import com.sprintkeyz.oxygenate.data.rememberPref
import com.sprintkeyz.oxygenate.data.setPref
import com.sprintkeyz.oxygenate.ui.components.items.SliderItem
import kotlin.time.DurationUnit

// "smart" slider that handles preferences!

@Composable
fun PrefsSliderItem(
    configItem: ConfigItem<out Number>,
    icon: ImageVector,
    title: String,
    subtitle: String,
    valueRange: IntProgression,
    stepSize: Int = 1,
    prefsDurationUnit: DurationUnit? = null,
    prefsIsPercentage: Boolean = false,
    disabled: Boolean = false,
    valuePreviewTemplate: (Long) -> String = { it.toString() }
) {
    val ctx = LocalContext.current

    // 1. Cast to Number, not Long.
    // This works because Float, Int, and Long all inherit from Number.
    @Suppress("UNCHECKED_CAST")
    val configAsNumber = configItem as ConfigItem<Number>

    // 2. rememberPref will now return a MutableState<Number>
    var currentValue by rememberPref(
        configItem = configAsNumber,
        durationUnit = prefsDurationUnit,
        percentage = prefsIsPercentage
    )

    SliderItem(
        icon = icon,
        title = title,
        subtitle = subtitle,
        value = currentValue.toLong(),
        valueRange = valueRange,
        stepSize = stepSize,
        disabled = disabled,
        valuePreviewTemplate = valuePreviewTemplate,
        onValueChange = { newValue ->
            // make sure we preserve the type
            currentValue = when (currentValue) {
                is Float -> newValue.toFloat()
                is Int -> newValue.toInt()
                else -> newValue
            }
            currentValue // fixes warning lmao
        },
        onValueChangeFinished = {
            when (configItem.pref.value) {
                is Float -> {
                    @Suppress("UNCHECKED_CAST")
                    setPref(ctx, configAsNumber as ConfigItem<Float>, currentValue.toFloat(), prefsDurationUnit, prefsIsPercentage)
                }
                is Int -> {
                    @Suppress("UNCHECKED_CAST")
                    setPref(ctx, configAsNumber as ConfigItem<Int>, currentValue.toInt(), prefsDurationUnit, prefsIsPercentage)
                }
                else -> {
                    @Suppress("UNCHECKED_CAST")
                    setPref(ctx, configAsNumber as ConfigItem<Long>, currentValue.toLong(), prefsDurationUnit, prefsIsPercentage)
                }
            }
        }
    )
}