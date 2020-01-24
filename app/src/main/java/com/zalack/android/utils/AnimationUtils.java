package com.zalack.android.utils;

import android.view.View;
import android.view.ViewGroup;

import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.security.acl.Group;

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

    public static void fadeAnimation(long duration, ViewGroup view) {
        Transition transition = new Fade();
        transition.setDuration(duration);
        TransitionManager.beginDelayedTransition(view, transition);
    }

}
