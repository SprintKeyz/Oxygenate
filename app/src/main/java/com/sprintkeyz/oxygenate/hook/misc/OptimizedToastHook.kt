package com.sprintkeyz.oxygenate.hook.misc

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.MiscDataConst
import com.sprintkeyz.oxygenate.data.isHookEnabled
import com.sprintkeyz.oxygenate.hook.AbstractHook

object OptimizedToastHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if this is true, we enable the hook
        return isHookEnabled(param, MiscDataConst.OPTIMIZED_TOAST_DISABLE)
    }

    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
            // this is  a much more foolproof method
            "android.widget.Toast".toClass().resolve().firstMethod {
                name = "show"
            }.hook {
                intercept()
            }
        }
    }

}