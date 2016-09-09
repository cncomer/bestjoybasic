package com.shwy.bestjoy.utils;

import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Created by bestjoy on 16/3/10.
 */
public class ScreenUtil {
    public static float getScreenDensity()  {
        return Globals.getApplication().getResources().getDisplayMetrics().density;
    }

    public static Point getScreenSize()  {
        try {
            DisplayMetrics displayMetrics = Globals.getApplication().getResources().getDisplayMetrics();
            if (displayMetrics.widthPixels <= displayMetrics.heightPixels) {
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else {
                return new Point(displayMetrics.heightPixels, displayMetrics.widthPixels);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new Point(SystemUtils.getScreenWidth(Globals.getApplication()), SystemUtils.getScreenHeight(Globals.getApplication()));
    }
}
