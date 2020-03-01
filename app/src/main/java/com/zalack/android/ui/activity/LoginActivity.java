package com.zalack.android.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.material.textfield.TextInputLayout;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.room.viewmodel.UserViewModel;
import com.zalack.android.data.webservice.viewmodel.LoginViewModel;
import com.zalack.android.ui.common.CustomInputEditText;
import com.zalack.android.utils.DialogUtils;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class LoginActivity extends BaseActivity implements DroidListener {

    @BindView(R.id.et_email)
    CustomInputEditText emailId;

    @BindView(R.id.et_password)
    CustomInputEditText password;

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    @BindView(R.id.button_submit)
    Button signInButton;

    @BindView(R.id.do_not_have_account)
    TextView notHaveAccount;

    @BindView(R.id.activity_login_container)
    ConstraintLayout parentLayout;

    @Inject
    ZalckPreferences prefs;

    UserViewModel userViewModel;
    private DroidNet mDroidNet;
    Dialog dialog;

    private LoginViewModel loginViewModel;
    boolean isValidEmail = false;
    boolean isValidPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setUpProgress(parentLayout);

        init();

        validateInputFields();
    }

    private void validateInputFields() {
        emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String error = UIUtils.isValid(LoginActivity.this, UIUtils.EMAIL, charSequence);
                if (error.isEmpty()) {
                    if (isValidPassword) {
                        signInButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    }
                    isValidEmail = true;
                    tilEmail.setError(null);
                } else {
                    tilEmail.setError(error);
                    emailId.setBackground(getResources().getDrawable(R.drawable.floating_edit_text_bg));
                    signInButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailId.setBackground(getResources().getDrawable(R.drawable.floating_edit_text_bg));
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String error = UIUtils.isValid(LoginActivity.this, UIUtils.PASSWORD, charSequence);
                if (error.isEmpty()) {
                    if (isValidEmail) {
                        signInButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    }
                    isValidPassword = true;
                    tilPassword.setError(null);
                } else {
                    tilPassword.setError(error);
                    signInButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));
                    password.setBackground(getResources().getDrawable(R.drawable.floating_edit_text_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

        if (isValidEmail && isValidPassword) {

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

        }

/*
        userViewModel.create(emailId.getText().toString(), password.getText().toString());
        userViewModel.getUsers().observe(this, users -> {
//            Log.e("Database", users.get(0).getId() + users.get(0).getPassword());
        });
*/
    }

    @OnClick(R.id.do_not_have_account)
    public void signUp() {
        this.finish();
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

    @OnClick(R.id.parent_constraint_layout)
    public void rootLayout() {
        UIUtils.hideKeyboard(this);
    }

    @OnEditorAction(R.id.et_password)
    public void editorAction() {
        signIn();
    }
}
