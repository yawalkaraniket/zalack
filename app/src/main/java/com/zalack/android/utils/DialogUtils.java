package com.zalack.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zalack.android.R;
import com.zalack.android.data.models.project_tickets.Ticket;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DialogUtils {

    public static String taskName, taskDescription;

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

    public static Dialog editTask(Activity activity, Ticket ticket, final View.OnClickListener submitClick) {
        View view = activity.getLayoutInflater().inflate(R.layout.update_task_dialog, null);

        final android.app.Dialog dialog = new android.app.Dialog(activity, R.style.Theme_D1NoTitleDim);
        dialog.setContentView(view);
        dialog.getWindow().setLayout((int) UIUtils.getScreenWidth(), MATCH_PARENT);
        try {
            dialog.show();
            UIUtils.hideKeyboard(activity);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
        EditText name = view.findViewById(R.id.task_name_edit);
        EditText description = view.findViewById(R.id.task_description_edit);
        Button cancelButton = view.findViewById(R.id.btn_cancel);
        Button submitButton = view.findViewById(R.id.btn_submit);

        name.setText(ticket.getName());
        description.setText(ticket.getDescription());

        submitButton.setOnClickListener(submit -> {
            taskName = name.getText().toString();
            taskDescription = description.getText().toString();
            submitClick.onClick(submit);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(outside -> {
            dialog.dismiss();
        });
        return dialog;
    }
}
