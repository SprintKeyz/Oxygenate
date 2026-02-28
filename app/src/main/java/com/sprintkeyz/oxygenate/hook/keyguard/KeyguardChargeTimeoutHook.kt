package com.sprintkeyz.oxygenate.hook.keyguard

import android.util.Log
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.KeyguardDataConst
import com.sprintkeyz.oxygenate.data.getPref
import com.sprintkeyz.oxygenate.data.isHookEnabled
import com.sprintkeyz.oxygenate.hook.AbstractHook

object KeyguardChargeTimeoutHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if this is true, we enable the hook
        return isHookEnabled(param, KeyguardDataConst.KEYGUARD_CHARGE_ANIM_EXTENSION)
    }

    // time out
    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
            "com.oplus.charge.viewmodel.OplusChargeAnimImpl".toClass()
                .resolve()
                .firstMethod {
                    name = "updateChargeAnimState"
                }
                .hook {
                    before {
                        val delayTime = getPref(param, KeyguardDataConst.KEYGUARD_CHARGE_ANIM_EXTENSION)

                        val arg0 = args[0]
                        val arg1 = args[1]

                        Log.d("TESTTAG", "Arg1 is $arg1, val is $delayTime")

                        if (arg0 == 4 && arg1 == "time out") {
                            result = null

                            // get our handler
                            // we want to use our existing handler because it's easier than trying to make a new one
                            // this one is also already correctly tied to the looper
                            val handler = instanceClass?.getDeclaredField("handler").let { field ->
                                field?.isAccessible = true
                                field?.get(instance) as? android.os.Handler
                            }

                            // add a delay
                            handler?.postDelayed({
                                try {
                                    // get the method to call it again
                                    val method = instance.javaClass.getDeclaredMethod(
                                        "updateChargeAnimState",
                                        Int::class.java,
                                        String::class.java
                                    )
                                    // make sure we don't catch this again
                                    method.invoke(instance, 4, "manual exit")
                                } catch (_: Exception) {
                                    // N/A
                                }
                            }, delayTime) // tunable delay
                        }
                    }
                }
        }
    }
}
