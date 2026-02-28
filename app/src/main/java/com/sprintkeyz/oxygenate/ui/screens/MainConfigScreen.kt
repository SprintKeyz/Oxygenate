package com.sprintkeyz.oxygenate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.sprintkeyz.oxygenate.data.ModifiedScopesManager
import com.sprintkeyz.oxygenate.ui.components.items.StatusItem
import com.sprintkeyz.oxygenate.ui.components.items.PillPopupHost
import com.sprintkeyz.oxygenate.ui.components.items.SectionHeaderItem
import com.sprintkeyz.oxygenate.ui.components.items.SubmenuItem
import com.sprintkeyz.oxygenate.ui.components.items.rememberPillPopupState
import com.sprintkeyz.oxygenate.utils.restartPackage
import com.sprintkeyz.oxygenate.utils.softReboot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainConfigScreen(
    isModuleActive: Boolean,
    isRooted: Boolean,
    onNavigateToStatusBar: () -> Unit,
    onNavigateToMisc: () -> Unit,
    onNavigateToKeyguard: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val popupState = rememberPillPopupState()
    val ctx = LocalContext.current
    val asyncScope = rememberCoroutineScope()

    fun handleApplyScopes() {
        // get current scopes
        val scopes = ModifiedScopesManager.getScopes(ctx)

        // check if we need to restart system
        if (scopes.contains("system")) {
            asyncScope.launch(Dispatchers.IO) {
                ModifiedScopesManager.removeAllScopes(ctx)
                delay(800) // this accounts for filesystem r/w issues
                softReboot()
            }
        }

        // lowest to highest from here
        if (scopes.contains("com.oplus.athena")) {
            ModifiedScopesManager.removeScope("com.oplus.athena", ctx)
            restartPackage("com.oplus.athena")
        }

        if (scopes.contains("com.android.launcher")) {
            ModifiedScopesManager.removeScope("com.android.launcher", ctx)
            restartPackage("com.android.launcher")
        }

        if (scopes.contains("com.android.systemui")) {
            ModifiedScopesManager.removeScope("com.android.systemui", ctx)
            restartPackage("com.android.systemui")
        }
    }

    fun checkReboot(scopes: Set<String>): Boolean {
        return scopes.contains("system")
    }

    PillPopupHost(state = popupState) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    title = {
                        Text(
                            "Preferences",
                            fontSize = lerp(
                                34.sp,
                                22.sp,
                                scrollBehavior.state.collapsedFraction
                            )
                        )

                    },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        // Restart SystemUI button
                        IconButton(onClick = {
                            val scopes = ModifiedScopesManager.getScopes(ctx)
                            val hasReboot = checkReboot(scopes)
                            val count = scopes.size

                            val msg = if (count == 0) {
                                "Nothing to apply!"
                            }
                            else if (hasReboot) {
                                "Reboot to apply?"
                            } else if (count == 1) {
                                "Restart $count process?"
                            } else {
                                "Restart $count processes?"
                            }

                            val btnText = if (count == 0) {
                                null
                            }
                            else if (hasReboot) {
                                "REBOOT"
                            } else {
                                "RESTART"
                            }

                            popupState.show(
                                msg,
                                actionText = btnText
                            ) {
                                handleApplyScopes()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = "Apply Changes",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
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
                SectionHeaderItem("Status")
                Spacer(modifier = Modifier.height(8.dp))

                StatusItem(
                    isActive = isModuleActive,
                    isRootAvailable = isRooted
                )

                Spacer(modifier = Modifier.height(8.dp))

                SectionHeaderItem("Features")
                Spacer(modifier = Modifier.height(8.dp))

                SubmenuItem(
                    icon = Icons.Default.SignalCellularAlt,
                    title = "Status Bar",
                    subtitle = "Status bar tweaks",
                    onClick = onNavigateToStatusBar
                )

                Spacer(modifier = Modifier.height(8.dp))

                SubmenuItem(
                    icon = Icons.Default.Lock,
                    title = "Lock Screen",
                    subtitle = "Lock Screen tweaks",
                    onClick = onNavigateToKeyguard
                )

                Spacer(modifier = Modifier.height(8.dp))

                SubmenuItem(
                    icon = Icons.Default.MiscellaneousServices,
                    title = "Misc. Features",
                    subtitle = "Tweak various system features",
                    onClick = onNavigateToMisc
                )
            }
        }
    }
}