package com.sprintkeyz.oxygenate.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
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

    override fun onHook() = YukiHookAPI.encase {
        // init our hooks
        loadApp("com.android.systemui") {
            RedOneHook.init(this)
        }

        loadApp("com.oplus.athena") {
            OptimizedToastHook.init(this)
        }
    }
}