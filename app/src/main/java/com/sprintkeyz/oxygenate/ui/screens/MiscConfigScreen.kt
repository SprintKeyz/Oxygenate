package com.sprintkeyz.oxygenate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExtensionOff
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.sprintkeyz.oxygenate.data.DisplayDataConst
import com.sprintkeyz.oxygenate.data.MiscDataConst
import com.sprintkeyz.oxygenate.data.rememberPref
import com.sprintkeyz.oxygenate.ui.components.PrefsSliderItem
import com.sprintkeyz.oxygenate.ui.components.PrefsSwitchItem
import com.sprintkeyz.oxygenate.ui.components.items.SectionHeaderItem
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiscConfigScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val autoBrightnessTweakState by rememberPref(DisplayDataConst.AUTO_BRIGHTNESS_TWEAKS)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Other Features",
                        fontSize = lerp(
                            34.sp,
                            22.sp,
                            scrollBehavior.state.collapsedFraction
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            SectionHeaderItem("Annoyances")
            Spacer(modifier = Modifier.height(8.dp))
            PrefsSwitchItem(
                icon = Icons.Default.ExtensionOff,
                title = "No Optimized Toast",
                subtitle = "Disable the toast message when closing all apps",
                configItem = MiscDataConst.OPTIMIZED_TOAST_DISABLE,
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrefsSwitchItem(
                icon = Icons.Default.FolderDelete,
                title = "Close Current App",
                subtitle = "Closing all apps clears the current app too",
                configItem = MiscDataConst.CLOSE_ALL_RECENT_APPS
            )

            Spacer(modifier = Modifier.height(8.dp))
            SectionHeaderItem("Display")
            Spacer(modifier = Modifier.height(8.dp))
            PrefsSwitchItem(
                icon = Icons.Default.BrightnessAuto,
                title = "Brightness Tweaks",
                subtitle = "Enable auto brightness algorithm tweaks",
                configItem = DisplayDataConst.AUTO_BRIGHTNESS_TWEAKS
            )

            Spacer(modifier = Modifier.height(8.dp))

            PrefsSliderItem(
                icon = Icons.Default.Edit,
                title = "Auto Brightness Curve",
                subtitle = "Modify the auto brightness curve",
                valueRange = -20..50 step 5,
                stepSize = 5,
                valuePreviewTemplate = { value ->
                    val prefix = if (value >= 0) "+" else ""
                    "$prefix${value}%"
                },
                prefsIsPercentage = true,
                configItem = DisplayDataConst.AUTO_BRIGHTNESS_MODIFIER,
                disabled = !autoBrightnessTweakState
            )

            Spacer(modifier = Modifier.height(8.dp))

            PrefsSliderItem(
                icon = Icons.Default.Timer,
                title = "Brightness Override",
                subtitle = "How long to wait after a slider adjustment to take back control",
                valueRange = 1..15,
                valuePreviewTemplate = { "$it min." },
                prefsDurationUnit = DurationUnit.MINUTES,
                configItem = DisplayDataConst.AUTO_BRIGHTNESS_OVERRIDE_TIMER,
                disabled = !autoBrightnessTweakState
            )
        }
    }
}