package com.sprintkeyz.oxygenate.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.sprintkeyz.oxygenate.hook.statusbar.RedOneHook

// to REBOOT SYSTEMUI: adb shell am crash com.android.systemui

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
            loadApp("com.android.systemui") {
                RedOneHook.init(this)
            }
    }
}