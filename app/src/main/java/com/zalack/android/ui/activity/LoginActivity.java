package com.zalack.android.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.room.viewmodel.UserViewModel;
import com.zalack.android.data.webservice.viewmodel.LoginViewModel;
import com.zalack.android.ui.common.FontEditText;
import com.zalack.android.utils.DialogUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements DroidListener {

    @BindView(R.id.et_email)
    FontEditText emailId;

    @BindView(R.id.et_password)
    FontEditText password;

    @BindView(R.id.button_submit)
    Button signInButton;

    @BindView(R.id.do_not_have_account)
    TextView notHaveAccount;

    @BindView(R.id.actvity_login_container)
    ConstraintLayout parentLayout;

    @Inject
    ZalckPreferences prefs;

    UserViewModel userViewModel;
    private DroidNet mDroidNet;
    Dialog dialog;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setUpProgress(parentLayout);

        init();
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        ((ZalckApp) getApplicationContext()).getMyComponent().inject(this);
    }

    @OnClick(R.id.button_submit)
    public void signIn() {

        HashMap<String, String> userMap = new HashMap();
        userMap.put("email", emailId.getText().toString());
        userMap.put("password", password.getText().toString());

        showProgress();
        loginViewModel.getUser(userMap).observe(this, login -> {
            hideProgress();
            switch (login.getStatus()) {
                case SUCCESS:
                    if (login.getData() != null) {
                        Login data = login.getData();
                        prefs.setToken(data.getTokenType().substring(0, 1).toUpperCase()
                                + data.getTokenType().substring(1) + " " + data.getAccessToken());
                        prefs.setEmail(data.getData().getUser().getEmail());
                        prefs.setName(data.getData().getUser().getFirstName() + " " + data.getData().getUser().getLastName());
                        prefs.setMobileNumber(String.valueOf(data.getData().getUser().getMobile()));

                        LoginActivity.this.finish();
                        Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                        startActivity(intent);
                    }
                    break;
                case ERROR:
                    Toast.makeText(this, "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
            }
        });

/*
        userViewModel.create(emailId.getText().toString(), password.getText().toString());
        userViewModel.getUsers().observe(this, users -> {
//            Log.e("Database", users.get(0).getId() + users.get(0).getPassword());
        });
*/
    }

    @OnClick(R.id.do_not_have_account)
    public void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userViewModel != null && loginViewModel != null) {
            userViewModel.clear();
            loginViewModel.clear();
        }
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
