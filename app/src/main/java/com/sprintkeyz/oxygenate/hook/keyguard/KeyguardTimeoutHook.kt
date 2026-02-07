package com.sprintkeyz.oxygenate.hook.keyguard

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.sprintkeyz.oxygenate.data.MiscDataConst
import com.sprintkeyz.oxygenate.hook.AbstractHook

/*
XposedHelpers.findAndHookMethod("com.android.server.power.PowerManagerService", classLoader, "getScreenOffTimeoutLocked", long.class, long.class, new XC_MethodHook() {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
    }
});

how this function works:

when the phone is unlocked, the function returns the screen timeout (milliseconds)
when the phone is locked, it returns the lock screen timeout (milliseconds), typically 10k

we could use this to straight up modify the screen off
maybe override the power saving mode screen timeout
or an infinite screen on?

within this function:

public long getScreenOffTimeoutLocked(long sleepTimeout, long attentiveTimeout) {
        long timeout = this.mScreenOffTimeoutSetting;
        if (isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked()) {
            timeout = Math.min(timeout, this.mMaximumScreenOffTimeoutFromDeviceAdmin);
        }
        if (this.mUserActivityTimeoutOverrideFromWindowManager >= 0) {
            timeout = Math.min(timeout, this.mUserActivityTimeoutOverrideFromWindowManager);
        }
        if (sleepTimeout >= 0) {
            timeout = Math.min(timeout, sleepTimeout);
        }
        if (attentiveTimeout >= 0) {
            timeout = Math.min(timeout, attentiveTimeout);
        }
        return Math.max(mPmsExt.getScreenOffTimeoutLocked(timeout), this.mMinimumScreenOffTimeoutConfig);
    }

when the phone is UNLOCKED, mUserActivityTimeoutOverrideFromWindowManager is set to -1. When the phone is LOCKED, it is 10000.

The other values are:
- mScreenOffTimeoutSetting (300000) / screen timeout device setting
- mMaximumScreenOffTimeoutFromDeviceAdmin (9223372036854775807) / max long?
- mMinimumScreenOffTimeoutConfig is 10000
- both args are -1


that value is set here:

XposedHelpers.findAndHookMethod("com.android.server.power.PowerManagerService", classLoader, "setUserActivityTimeoutOverrideFromWindowManagerInternal", long.class, new XC_MethodHook() {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
    }
});

this is the master function that "should" be safe to override. it only updates if the value changes:

if (this.mUserActivityTimeoutOverrideFromWindowManager != timeoutMillis) {
                if (DEBUG_PANIC || DEBUG) {
                    Slog.d(TAG, "UA TimeoutOverrideFromWindowManagerInternal = " + timeoutMillis);
                }
                this.mUserActivityTimeoutOverrideFromWindowManager = timeoutMillis;
                com.android.server.EventLogTags.writeUserActivityTimeoutOverride(timeoutMillis);
                this.mDirty |= 32;
                updatePowerStateLocked();
            }

and this variable mUserActivityTimeoutOverrideFromWindowManager only seems to be used in the screen off timer.

so we just override args[0] with our desired value when called? will this function not constantly spam?
it does seem to update less

we can't just update the arg for some unknown reason, so we'll replace the variable after checking its value...

oh wait, we probably can. The problem is that the way the member variable is used is not the whole calculation.

We do need to override the previous function.
 */

object KeyguardTimeoutHook : AbstractHook() {
    override fun isEnabled(param: PackageParam): Boolean {
        // if this is true, we enable the hook
        return param.prefs.get(MiscDataConst.LOCK_SCREEN_TIMEOUT_DISABLED_STATE)
    }

    override fun onInit(param: PackageParam) {
        // enter our scope
        param.apply {
            "com.android.server.power.PowerManagerService".toClass()
                .resolve()
                .firstMethod {
                    name = "getScreenOffTimeoutLocked"
                }
                .hook {
                    after {
                        // check if phone is locked
                        val lockedCheck = instanceClass?.getDeclaredField("mUserActivityTimeoutOverrideFromWindowManager").let { field ->
                            field?.isAccessible = true;
                            field?.get(instance) as? Long
                        }

                        if (lockedCheck == -1L) {
                            return@after
                        }

                        // device is locked, update. make it 12h?
                        result = 43200000L
                    }
                }

            YLog.info("Test hook installed successfully")
        }
    }
}