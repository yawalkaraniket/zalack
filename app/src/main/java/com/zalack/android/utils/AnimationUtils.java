package com.zalack.android.utils;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class AnimationUtils {

    public static void runAnimation(View view, Techniques techniques) {
        YoYo.with(techniques)
                .duration(200)
                .repeat(1)
                .playOn(view);
    }

    public static void runAnimation(View view, Techniques techniques, int duration) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(1)
                .playOn(view);
    }

    public static void runAnimation(View view, Techniques techniques, int duration, int repeatCount) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(repeatCount)
                .playOn(view);
    }

}
