package com.shwy.bestjoy.utils;

import android.content.Context;

/**
 * Created by bestjoy on 16/3/10.
 */
public class DensityUtil {

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int sp2px(Context paramContext, float spValue) {
        return (int) (spValue * paramContext.getResources().getDisplayMetrics().scaledDensity);
    }

}
