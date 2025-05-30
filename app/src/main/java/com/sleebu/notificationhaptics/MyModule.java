package com.sleebu.notificationhaptics;
import android.os.VibrationEffect;
import android.util.TypedValue;
import android.widget.TextView;
import java.util.Objects;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedBridge;

public class MyModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(Objects.equals(lpparam.packageName, "com.android.systemui")){
            XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    TextView tv = (TextView) param.thisObject;
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
            });
            return;
        }

        if(Objects.equals(lpparam.packageName, "android")){
            XposedHelpers.findAndHookMethod("android.app.NotificationChannel", lpparam.classLoader, "getVibrationEffect", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    try {
                        VibrationEffect effect = VibrationEffect.createOneShot(300, -1);
                        param.setResult(effect);
                    }
                    catch(Exception e){
                        XposedBridge.log("could not set result effect");
                    }
                }
            });
        }
    }
}

