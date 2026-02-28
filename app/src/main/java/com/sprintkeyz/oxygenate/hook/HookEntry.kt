package com.sprintkeyz.oxygenate.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.core.annotation.LegacyResourcesHook
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.sprintkeyz.oxygenate.hook.keyguard.KeyguardChargeTimeoutHook
import com.sprintkeyz.oxygenate.hook.keyguard.KeyguardTimeoutHook
import com.sprintkeyz.oxygenate.hook.misc.AutoBrightnessHook
import com.sprintkeyz.oxygenate.hook.misc.CloseAllRecentsHook
import com.sprintkeyz.oxygenate.hook.misc.OptimizedToastHook
import com.sprintkeyz.oxygenate.hook.statusbar.RedOneHook

@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {

    override fun onInit() {
        YukiHookAPI.configs {
            debugLog {
                tag = "Oxygenate"
                isEnable = true
            }
            isDebug = true
        }

    }

    @OptIn(LegacyResourcesHook::class)
    override fun onHook() = YukiHookAPI.encase {
        // init our hooks
        loadApp("com.android.systemui") {
            RedOneHook.init(this)
            KeyguardChargeTimeoutHook.init(this)
        }

        loadApp("android") {
            KeyguardTimeoutHook.init(this)
            AutoBrightnessHook.init(this)
        }

        loadApp("com.oplus.athena") {
            OptimizedToastHook.init(this)
        }

        loadApp("com.android.launcher") {
            CloseAllRecentsHook.init(this)
        }
    }
}