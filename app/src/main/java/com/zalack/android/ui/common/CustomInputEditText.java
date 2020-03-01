package com.zalack.android.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.zalack.android.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CustomInputEditText extends TextInputEditText {
    public CustomInputEditText(Context context) {
        super(context);
    }

    public CustomInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        String customFont = a.getString(R.styleable.FontTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }


    public boolean setCustomFont(Context ctx, String asset) {
        if (asset != null) {
            Typeface tf = null;
            try {
                tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            } catch (Exception e) {
                Log.e(TAG, "Could not get typeface: " + e.getMessage());
                return false;
            }

            setTypeface(tf);
        } else {
            //set default typeface light
            setDefaultTypeface(ctx);
        }
        return true;
    }

    private void setDefaultTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_bold_averta)));
    }
}
