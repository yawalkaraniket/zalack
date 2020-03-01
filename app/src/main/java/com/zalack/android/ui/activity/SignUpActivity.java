package com.zalack.android.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.webservice.viewmodel.SignUpViewModel;
import com.zalack.android.ui.adapters.NavigationPagerAdapter;
import com.zalack.android.ui.adapters.OnboardingPagerAdapter;
import com.zalack.android.ui.common.CustomInputEditText;
import com.zalack.android.ui.common.FontEditText;
import com.zalack.android.ui.common.NavigationPager;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.sign_up_activity_container)
    ConstraintLayout constraintLayout;

    @BindView(R.id.onboarding_pager)
    NavigationPager pager;

    @Inject
    ZalckPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setUpProgress(constraintLayout);
        init();
    }

    private void init() {
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager(), this));
    }

    @OnClick(R.id.already_have_an_account)
    public void alreadyHaveAnAccount() {
        this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openAddEmailScreen() {
        pager.setCurrentItem(1, true);
    }

    public void openAddMobileScreen() {
        pager.setCurrentItem(2, true);
    }

    public void openAddPasswordScreen() {
        pager.setCurrentItem(3, true);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() > 0) {
            pager.setCurrentItem(pager.getCurrentItem()-1);
        } else {
            super.onBackPressed();
        }
    }
}
