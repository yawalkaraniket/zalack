package com.zalack.android.ui.common;

/**
 * Created by nataraj on 9/24/15.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;
import com.zalack.android.R;

//easy way to set font right in xml file
public class FontTextView extends AppCompatTextView {
    private static final String TAG = "TextView";
    private Paint background = new Paint();
    boolean whiteBackground;

    public FontTextView(Context context) {
        super(context);
        //set light font as default
        setDefaultTypeface(context);
        init(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
        init(context, attrs);
        //set light font as default
        //   setDefaultTypeface(context);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
        init(context, attrs);
        //set light font as default
        //  setDefaultTypeface(context);
    }

    private void init(Context context, AttributeSet attrs) {
        background.setColor(getResources().getColor(R.color.white));
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
            whiteBackground = a.getBoolean(R.styleable.FontTextView_whiteBackground, false);
            a.recycle();
        }
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