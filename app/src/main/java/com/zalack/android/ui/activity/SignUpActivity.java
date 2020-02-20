package com.zalack.android.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zalack.android.R;
import com.zalack.android.ui.common.FontEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    FontEditText name;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.already_have_an_account)
    public void alreadyHaveAnAccount() {
        this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
