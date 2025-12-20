package com.sprintkeyz.oxygenate.hook

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.sprintkeyz.oxygenate.data.DataConst

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
                // Get preference using YukiHookAPI's prefs system
                val isEnabled = prefs.get(DataConst.MODULE_ENABLED)

                YLog.info("Module enabled status: $isEnabled")

                // Only hook if enabled
                if (!isEnabled) {
                    YLog.info("Module is disabled, skipping hook")
                    return@loadApp
                }

                "com.android.systemui.statusbar.policy.Clock".toClass()
                    .resolve()
                    .firstMethod {
                        name = "updateClock"
                        emptyParameters()
                    }
                    .hook {
                        after {
                            val tv = instance<TextView>()
                            val text = tv.text.toString()

                            if (text.isNotEmpty() /*&& text[0] == '1'*/) {
                                val spannable = SpannableString(text)
                                spannable.setSpan(
                                    ForegroundColorSpan(Color.RED),
                                    0,
                                    1,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )

                                tv.text = spannable
                            }
                        }
                    }

                YLog.info("Clock hook installed successfully")
            }
    }
}