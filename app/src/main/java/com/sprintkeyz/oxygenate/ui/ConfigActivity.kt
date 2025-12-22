package com.sprintkeyz.oxygenate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.highcapable.yukihookapi.YukiHookAPI
import com.sprintkeyz.oxygenate.ui.navigation.NavHost
import com.sprintkeyz.oxygenate.ui.theme.OxygenateTheme
import com.sprintkeyz.oxygenate.utils.isRootAvailable

// two status bar tweak ideas:
// red 1 always visible
// disable red 1

// disable red 1 is the top setting, if that is enabled then red 1 always visible is disabled and greyed out

class ConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // start nav host
        enableEdgeToEdge()
        setContent {
            OxygenateTheme {
                NavHost(
                    isModuleActive = YukiHookAPI.Status.isModuleActive,
                    isRooted = isRootAvailable(),
                    context = this
                )
            }
        }
    }
}