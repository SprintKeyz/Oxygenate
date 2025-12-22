package com.sprintkeyz.oxygenate.hook.misc

import android.os.Bundle
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.MiscDataConst
import com.sprintkeyz.oxygenate.hook.AbstractHook

object OptimizedToastHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if this is true, we enable the hook
        return param.prefs.get(MiscDataConst.OPTIMIZATION_TOAST_DISABLED_STATE)
    }

    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
            "com.oplus.athena.client.AthenaService".toClass()
                .resolve()
                .firstMethod {
                    name = "i0"
                    parameters(Bundle::class)
                }
                .hook {
                    // no return value so can just intercept it
                    intercept()
                }
        }
    }

}