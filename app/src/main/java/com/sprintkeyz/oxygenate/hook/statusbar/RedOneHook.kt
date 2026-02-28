package com.sprintkeyz.oxygenate.hook.statusbar

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.StatusBarConfig
import com.sprintkeyz.oxygenate.data.isHookEnabled
import com.sprintkeyz.oxygenate.hook.AbstractHook

object RedOneHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if NOT default, enable the tweak
        return isHookEnabled(param, StatusBarConfig.RED_ONE_ALWAYS_IN_STATUS_BAR)
    }

    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
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

                        if (text.isNotEmpty() && text[0] == '1') {
                            val endIndex = if (text[1] == '1') 2 else 1
                            val spannable = SpannableString(text)

                            spannable.setSpan(
                                ForegroundColorSpan(
                                    Color.rgb(234, 22, 34)
                                ),
                                0,
                                endIndex,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannable.setSpan(
                                StyleSpan(Typeface.BOLD),
                                0,
                                endIndex,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            tv.text = spannable
                        }
                    }
                }
        }
    }

}