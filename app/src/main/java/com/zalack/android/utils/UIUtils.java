package com.zalack.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zalack.android.R;

public class UIUtils {

    public final static String EMAIL = "Email";
    public final static String PASSWORD = "Password";
    public final static String NAME = "Name";
    public final static String LAST_NAME = "Last Name";
    public final static String MOBILE = "Mobile";

    public static float dp() {
        DisplayMetrics metrics = getDisplayMetrics();
        return (float) metrics.densityDpi / 160f;
    }

    private static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static float getScreenWidth() {
        DisplayMetrics outMetrics = Resources.getSystem().getDisplayMetrics();
        return outMetrics.widthPixels;
    }

    public static float getScreenHeight() {
        DisplayMetrics outMetrics = Resources.getSystem().getDisplayMetrics();
        return outMetrics.heightPixels;
    }

    public static String isValid(Context context, String validationFor, CharSequence validateTo) {
        String message = "", pattern = "";
        String text = validateTo.toString();
        if (text.isEmpty()) {
            return context.getString(R.string.field_should_not_be_empty);
        }
        if (validateTo.length() > 1) {
            switch (validationFor) {
                case EMAIL:
                    pattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                    return text.matches(pattern) ? "" : context.getString(R.string.enter_valid_email);
                case PASSWORD:
                    pattern = "(.)*(\\d)(.)*";
                    if (!text.matches(pattern)) {
                        return context.getString(R.string.password_should_have_one_numeric_character);
                    }
                    if (text.matches("[a-zA-Z0-9]*")) {
                        return context.getString(R.string.password_should_have_one_special_character);
                    }
                    if (text.length() < 8) {
                        return context.getString(R.string.password_must_have_more_than_eight_character);
                    }
                    break;
                case NAME:
                    return text.matches("[a-zA-Z]*") ? "" : context.getString(R.string.enter_valid_name);
                case LAST_NAME:
                    return text.matches("[a-zA-Z]*") ? "" : context.getString(R.string.enter_valid_last_name);
                case MOBILE:
                    if (text.matches("[a-zA-Z]*")) {
                        return text.matches("[a-zA-Z]*") ? "" : context.getString(R.string.mobile_name_have_numbers);
                    }
                    if (text.length() > 10) {
                        return context.getString(R.string.number_must_have_10_digits);
                    }
                    break;
            }
        }
        return message;
    }
}
