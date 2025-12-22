package com.sprintkeyz.oxygenate.hook.statusbar

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.UIDataConst
import com.sprintkeyz.oxygenate.data.VisibilityState
import com.sprintkeyz.oxygenate.hook.AbstractHook

// TODO: There is a small bug here
// bug details:
// if the state is set to always disabled, the color
// of the first and second 1 in the status bar
// is too dark when swiping into the notification shade

// it seems like the notif shade uses a slightly different clock
// color than the status bar? not a problem for the red '1'
// effect since it just uses the normal notif center color

// it also causes some weird color bugs, apparently the
// status bar color adapts to the content (DUH!) and
// static colors won't work

// solution: "disabled" state is too broken for now :(

object RedOneHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if NOT default, enable the tweak
        return VisibilityState.valueOf(
            param.prefs.get(UIDataConst.HOOK_RED_ONE_VISIBILITY_STATE)
        ) != VisibilityState.DEFAULT
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
                            // our color is controlled by visibility state
                            // at this point in the code, it could either be always (red) or never (white)
                            val prefState = VisibilityState.valueOf(
                                param.prefs.get(UIDataConst.HOOK_RED_ONE_VISIBILITY_STATE)
                            )
                            spannable.setSpan(
                                ForegroundColorSpan(
                                    if (prefState == VisibilityState.ALWAYS)
                                        Color.rgb(234, 22, 34)
                                    else
                                        Color.rgb(207, 208, 210)
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

            YLog.info("Clock hook installed successfully")
        }
    }

}