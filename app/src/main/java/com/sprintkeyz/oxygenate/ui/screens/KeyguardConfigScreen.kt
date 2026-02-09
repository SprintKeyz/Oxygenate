package com.sprintkeyz.oxygenate.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.highcapable.yukihookapi.hook.factory.prefs
import com.sprintkeyz.oxygenate.data.MiscDataConst
import com.sprintkeyz.oxygenate.ui.components.PillPopupHost
import com.sprintkeyz.oxygenate.ui.components.SectionHeaderItem
import com.sprintkeyz.oxygenate.ui.components.SliderItem
import com.sprintkeyz.oxygenate.ui.components.SwitchItem
import com.sprintkeyz.oxygenate.ui.components.rememberPillPopupState
import com.sprintkeyz.oxygenate.utils.restartPackage
import com.sprintkeyz.oxygenate.utils.softReboot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyguardConfigScreen(
    onBackClick: () -> Unit,
    context: Context
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val popupState = rememberPillPopupState()

    var lockScreenTimeoutDisabledState by remember {
        mutableStateOf(context.prefs().get(
            MiscDataConst.LOCK_SCREEN_TIMEOUT_DISABLED_STATE
        ))
    }

    var lockScreenChargingAnimTimeoutExtension by remember {
        mutableLongStateOf(context.prefs().get(
            MiscDataConst.LOCK_SCREEN_CHARGING_ANIM_TIMEOUT_EXTENSION
        ) / 1000L)
    }

    fun onLockScreenTimeoutDisabledChange(isDisabled: Boolean) {
        context.prefs().edit {
            put(MiscDataConst.LOCK_SCREEN_TIMEOUT_DISABLED_STATE, isDisabled)
        }

        lockScreenTimeoutDisabledState = isDisabled

        popupState.show("Restart System?", "RESTART") {
            softReboot()
        }
    }

    fun onLockScreenChargingAnimTimeoutExtensionChange(extension: Int) {
        lockScreenChargingAnimTimeoutExtension = extension.toLong()
    }

    fun onSliderRelease() {
        context.prefs().edit {
            // Save the final value (multiplied by 1000)
            put(
                MiscDataConst.LOCK_SCREEN_CHARGING_ANIM_TIMEOUT_EXTENSION,
                lockScreenChargingAnimTimeoutExtension * 1000L
            )
        }

        popupState.show("Restart SystemUI?", "RESTART") {
            restartPackage("com.android.systemui")
        }
    }

    PillPopupHost(state = popupState) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    title = {
                        Text(
                            "Lock Screen",
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
                SectionHeaderItem("Appearance")
                Spacer(modifier = Modifier.height(8.dp))
                SliderItem(
                    icon = Icons.Default.Animation,
                    title = "Extend Charge Anim.",
                    subtitle = "Extend the display time of the charging animation",
                    value = lockScreenChargingAnimTimeoutExtension.toInt(),
                    valueRange = 0..7,
                    valuePreviewTemplate = { "${it}s" },
                    onValueChange = ::onLockScreenChargingAnimTimeoutExtensionChange,
                    onValueChangeFinished = ::onSliderRelease
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeaderItem("Misc")
                Spacer(modifier = Modifier.height(8.dp))
                SwitchItem(
                    icon = Icons.Default.Nightlight,
                    title = "Disable Timeout",
                    subtitle = "Keep the lock screen on",
                    checked = lockScreenTimeoutDisabledState,
                    onCheckedChange = ::onLockScreenTimeoutDisabledChange
                )
            }
        }
    }
}