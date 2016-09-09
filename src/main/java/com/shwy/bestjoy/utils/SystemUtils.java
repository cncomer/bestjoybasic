package com.shwy.bestjoy.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Iterator;

/**
 * Created by bestjoy on 16/3/10.
 */
public class SystemUtils {

    public static int getScreenHeight(Context context) {
        if (context != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    public static int getSystemVersion() {
        int systemVersion = 0;
        try {
            systemVersion = Build.VERSION.class.getField("SDK_INT").getInt(null);
        } catch (Exception exception1) {
            try {
                systemVersion = Integer.parseInt((String) Build.VERSION.class.getField("SDK").get(null));
            } catch (Exception exception2) {
                exception2.printStackTrace();
            }
        }
        return systemVersion;
    }

    public static boolean isEmulator(Context context) {
        String deviceId = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            deviceId = telephonyManager.getDeviceId();
        }
        return ((deviceId == null) || (deviceId.equals("000000000000000")));
    }

    public static boolean isServiceRunning(Context context, String className) {
        if (context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                Iterator<ActivityManager.RunningServiceInfo> iterator = activityManager.getRunningServices(30).iterator();
                while(iterator.hasNext()) {
                    ActivityManager.RunningServiceInfo serviceInfo = iterator.next();
                    if (serviceInfo.service.getClassName().contains(className)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
