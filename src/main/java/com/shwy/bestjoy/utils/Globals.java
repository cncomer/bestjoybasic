package com.shwy.bestjoy.utils;

import android.app.Application;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by bestjoy on 16/3/10.
 */
public class Globals {
    static Application sApplication;
    static ClassLoader sClassLoader;
    static String sInstalledVersionName;

    public static Application getApplication() {
        try {
            if (sApplication == null) {
                sApplication = getSystemApp();
            }
        } finally {
        }
        return sApplication;
    }

    public static ClassLoader getClassLoader() {
        try {
            if (sClassLoader == null) {
                sClassLoader = getApplication().getClassLoader();
            }
        } finally {
        }
        return sClassLoader;
    }

    public static String getInstalledVersionName() {
        return sInstalledVersionName;
    }

    private static Application getSystemApp() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field mInitialApplicationField = activityThreadClass.getDeclaredField("mInitialApplication");
            mInitialApplicationField.setAccessible(true);
            Application systemApplication = (Application) mInitialApplicationField.get(currentActivityThreadMethod.invoke(null, new Object[0]));
            return systemApplication;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode() {
        String pkgName = getApplication().getPackageName();
        try {
            return getApplication().getPackageManager().getPackageInfo(pkgName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName() {
        try {
            return getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
        return "1.0.0-alpha";
    }
}
