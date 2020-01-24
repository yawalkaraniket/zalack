package com.zalack.android.ui.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProviders;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.zalack.android.R;
import com.zalack.android.ZalackApp;
import com.zalack.android.data.ZalackPreferences;
import com.zalack.android.data.room.viewmodel.UserViewModel;
import com.zalack.android.ui.common.FontEditText;
import com.zalack.android.ui.common.FontTextView;
import com.zalack.android.utils.DialogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements DroidListener {

    @BindView(R.id.emailId)
    FontEditText emailId;

    @BindView(R.id.password)
    FontEditText password;

    @BindView(R.id.sign_in_button)
    FontTextView signInButton;

    @Inject
    ZalackPreferences prefs;

    UserViewModel userViewModel;
    private DroidNet mDroidNet;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);

        ((ZalackApp)getApplicationContext()).getMyComponent().inject(this);
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {

        userViewModel.create(emailId.getText().toString(), password.getText().toString());
        userViewModel.getUsers().observe(this, users -> {
//            Log.e("Database", users.get(0).getId() + users.get(0).getPassword());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userViewModel.clear();
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (!isConnected) {
            dialog = DialogUtils.networkDialog(this);
        } else if (dialog != null) {
            dialog.dismiss();
        }
    }
}
