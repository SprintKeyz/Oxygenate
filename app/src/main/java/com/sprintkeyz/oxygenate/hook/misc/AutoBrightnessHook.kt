package com.sprintkeyz.oxygenate.hook.misc

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.DisplayDataConst
import com.sprintkeyz.oxygenate.data.getPref
import com.sprintkeyz.oxygenate.data.isHookEnabled
import com.sprintkeyz.oxygenate.hook.AbstractHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object AutoBrightnessHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        return isHookEnabled(param, DisplayDataConst.AUTO_BRIGHTNESS_TWEAKS)
    }

    fun normalizeBrightness(value: Float, min: Float, max: Float): Float {
        // convert our value to a 0-1 float
        return (value - min) / (max - min)
    }

    fun denormalizeBrightness(normalized: Float, min: Float, max: Float): Float {
        return min + normalized * (max - min)
    }

    fun curveBrightness(param: PackageParam, value: Float, min: Float, max: Float): Float {
        // I would rather not touch values above the max (this includes high brightness range)
        if (value > max) return value

        // normalize
        val normalized = normalizeBrightness(value, min, max)
        val curve = getPref(param, DisplayDataConst.AUTO_BRIGHTNESS_MODIFIER)

        // curve
        val curved = (normalized + curve).coerceIn(0f, 1f)

        // return
        return denormalizeBrightness(curved, min, max)
    }

    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
            "com.android.server.display.OplusDisplayPowerControllerExtImpl".toClass()
                .resolve()
                .firstMethod {
                    name = "handleDisplayChanged"
                }
                .hook {
                    // this should persist until reboot
                    var lastTempAdjustTime = 0L

                    before {
                        val overrideTimer = getPref(param, DisplayDataConst.AUTO_BRIGHTNESS_OVERRIDE_TIMER)

                        val brightness = args[2] as Float
                        val reason = args[5]

                        val min = XposedHelpers.callMethod(instance, "getMinDisplayBrightness") as Float
                        val max = XposedHelpers.callMethod(instance, "getMaxDisplayBrightness") as Float

                        if (reason.toString() == "temporary") {
                            // skip curve and add skip wait
                            lastTempAdjustTime = System.currentTimeMillis()
                            return@before
                        }

                        if (reason.toString() == "manual") {
                            // skip curve
                            lastTempAdjustTime = 0
                            return@before
                        }

                        if (reason.toString() == "automatic") {
                            if (System.currentTimeMillis() - lastTempAdjustTime < overrideTimer) {
                                XposedBridge.log("BRIGHTNESS_UPDATE: Ignoring curve")
                                return@before
                            }

                            val curved = curveBrightness(param, brightness, min, max)
                            args[2] = curved

                            XposedBridge.log("BRIGHTNESS_UPDATE: $brightness -> ${args[2]}")
                        }
                    }

                }
        }
    }

}