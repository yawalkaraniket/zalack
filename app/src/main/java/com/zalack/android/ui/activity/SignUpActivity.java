package com.zalack.android.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.webservice.viewmodel.SignUpViewModel;
import com.zalack.android.ui.common.FontEditText;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.et_first_name)
    FontEditText firstName;

    @BindView(R.id.et_last_name)
    FontEditText lastName;

    @BindView(R.id.et_email)
    FontEditText email;

    @BindView(R.id.et_mobile)
    FontEditText mobile;

    @BindView(R.id.et_password)
    FontEditText password;

    @BindView(R.id.et_confirm_password)
    FontEditText conformPassword;

    @BindView(R.id.already_have_an_account)
    TextView alreadyHaveAnAccount;

    @BindView(R.id.btn_signUp)
    Button signUpButton;

    @BindView(R.id.sign_up_activity_container)
    ConstraintLayout constraintLayout;

    @BindView(R.id.accept_termas_and_condition)
    AppCompatCheckBox checkBox;

    @Inject
    ZalckPreferences prefs;

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setUpProgress(constraintLayout);
        init();
    }

    private void init() {
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        ((ZalckApp) getApplicationContext()).getMyComponent().inject(this);
    }

    @OnClick(R.id.already_have_an_account)
    public void alreadyHaveAnAccount() {
        this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_signUp)
    public void signUp() {
        if (checkBox.isChecked()) {
            String name = firstName.getText().toString() + " " + lastName.getText().toString();
            String mobileNumber = mobile.getText().toString();
            String emailId = email.getText().toString();
            HashMap<String, String> user = new HashMap<>();
            user.put("email", emailId);
            user.put("password", password.getText().toString());
            user.put("first_name", firstName.getText().toString());
            user.put("last_name", lastName.getText().toString());
            user.put("confirmed", conformPassword.getText().toString());
            user.put("mobile", mobileNumber);

            showProgress();
            signUpViewModel.registerUser(user).observe(this, response -> {
                hideProgress();
                SignUp data = response.getData();
                switch (response.getStatus()) {
                    case SUCCESS:
                        prefs.setToken(data.getTokenType().substring(0, 1).toUpperCase()
                                + data.getTokenType().substring(1) + " " + data.getAccessToken());
                        prefs.setName(name);
                        prefs.setEmail(emailId);
                        prefs.setMobileNumber(mobileNumber);
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        this.finish();
                        Intent intent = new Intent(this, NavigationActivity.class);
                        startActivity(intent);
                        break;
                    case ERROR:
                        Toast.makeText(this, "Registration failed.\n Please try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        } else {
            Toast.makeText(this, "Please accept 'Terms and Conditions'", Toast.LENGTH_SHORT).show();
        }
    }

    @OnEditorAction(R.id.et_confirm_password)
    public void imeSignUpButton() {
        signUp();
    }

    @OnClick(R.id.scroll_container)
    public void rootContainer() {
        UIUtils.hideKeyboard(this);
    }
}
