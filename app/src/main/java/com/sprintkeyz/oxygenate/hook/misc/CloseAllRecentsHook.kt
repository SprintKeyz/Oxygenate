package com.sprintkeyz.oxygenate.hook.misc

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.MiscDataConst
import com.sprintkeyz.oxygenate.data.isHookEnabled
import com.sprintkeyz.oxygenate.hook.AbstractHook

object CloseAllRecentsHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if this is true, we enable the hook
        return isHookEnabled(param, MiscDataConst.CLOSE_ALL_RECENT_APPS)
    }

    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
            "com.android.quickstep.views.RecentsView".toClass()
                .resolve()
                .firstMethod {
                    name = "getTaskIdsForTaskViewId"
                }
                .hook {
                    // overwrite first arg to -1 (simulate home)
                    before {
                        args(0).set(-1)
                    }
                }
        }
    }

}