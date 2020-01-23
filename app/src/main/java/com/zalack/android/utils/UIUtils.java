package com.zalack.android.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class UIUtils {

    public static float dp() {
        DisplayMetrics metrics = getDisplayMetrics();
        return (float) metrics.densityDpi / 160f;
    }

    private static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }
}
