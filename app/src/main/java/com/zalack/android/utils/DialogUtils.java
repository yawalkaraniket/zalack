package com.zalack.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.zalack.android.R;

public class DialogUtils {

    public static Dialog networkDialog(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.no_network, null);

        final android.app.Dialog dialog = new android.app.Dialog(activity, R.style.dialog_style);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        try {
            dialog.show();
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }

        return dialog;

    }
}
