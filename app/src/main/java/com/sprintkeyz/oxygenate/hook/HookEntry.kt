package com.sprintkeyz.oxygenate.hook

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.field
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

        loadApp("com.android.launcher") {
            apply {
                val viewImplClass = "com.android.quickstep.views.RecentsView".toClass()

                viewImplClass.resolve()
                    .firstMethod { name = "getTaskIdsForTaskViewId" }
                    .hook {
                        /*before {
                            val instance = args[0] ?: return@before

                            // Helper function to safely get and print fields
                            fun logField(fieldName: String) {
                                try {
                                    val value = viewImplClass.field { name = fieldName }.get(instance).any()
                                    Log.d("HOOK_MANUAL", "$fieldName: $value")
                                } catch (e: Throwable) {
                                    // Field might not exist in this specific build
                                }
                            }

                            //viewImplClass.field { name = "springAnimToRecent" }.get(instance).set(true)

                            Log.d("HOOK_MANUAL", "--- MANUAL DUMP START ---")

                            // 1. State & Origin Flags
                            logField("mRunningTaskViewId")

                            Log.d("HOOK_MANUAL", "--- MANUAL DUMP END ---")
                            Toast.makeText(appContext, "Manual Dump Complete", Toast.LENGTH_SHORT).show()
                        }*/

                        before {
                            args(0).set(-1)
                        }

                    }
            }
        }
    }
}

/*
SOME NOTES

before {
                            val self = instance

                            val runningTaskId = self::class.java
                                .field {
                                    name = "mRunningTaskViewId"
                                    superClass(true)
                                }
                                .get(self)
                                .any()

                            Log.d("HOOK", "mRunningTaskViewId = $runningTaskId")
                        }

                        THAT CODE ABOVE WILL READ A MEMBER VARIABLE JUST GIVEN A FUNCTION IN A CLASS
                        IF IT ERRORS, TRY REMOVING THE superClass call.

 */

/*
THIS WORKS

before {
                            val self = instance

                            val runningTaskId = self::class.java
                                .field {
                                    name = "mRunningTaskViewId"
                                    superClass(true)
                                }
                                .get(self)
                                .set(-1)

                            Log.d("HOOK", "mRunningTaskViewId = $runningTaskId")
                        }

                        when hooking com.android.quickstep.views.RecentsView, getTaskIdsForTaskViewId
 */

// or this: before {
//                            args(0).set(-1)
//                        }