package com.zalack.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.webservice.viewmodel.SignUpViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.ui.activity.OnboardingActivity;
import com.zalack.android.ui.activity.SignUpActivity;
import com.zalack.android.ui.common.CustomInputEditText;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class OnboardingFragment extends BaseFragment {

    @BindView(R.id.et_first_name)
    CustomInputEditText firstName;

    @BindView(R.id.et_last_name)
    CustomInputEditText lastName;

    @BindView(R.id.et_email)
    CustomInputEditText email;

    @BindView(R.id.et_mobile)
    CustomInputEditText mobile;

    @BindView(R.id.et_password)
    CustomInputEditText password;

    @BindView(R.id.et_confirm_password)
    CustomInputEditText conformPassword;

    @BindView(R.id.til_first_name)
    TextInputLayout tilFirstName;

    @BindView(R.id.til_last_name)
    TextInputLayout tilLastName;

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;

    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    @BindView(R.id.til_confirm_password)
    TextInputLayout tilConfirmPassword;

    @BindView(R.id.already_have_an_account)
    TextView alreadyHaveAnAccount;

    @BindView(R.id.btn_signUp)
    Button signUpButton;

    @BindView(R.id.accept_termas_and_condition)
    AppCompatCheckBox checkBox;

    @Inject
    ZalckPreferences prefs;

    private SignUpViewModel signUpViewModel;
    private boolean isValidFirstName, isValidLastName, isValidEmail, isValidMobileNumber, isValidPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_onboarding, container, false);

        ButterKnife.bind(this, layout);
        setUpProgress(layout, this.getActivity());

        init();

        return layout;
    }

    private void init() {
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        ((ZalckApp) this.getContext()).getMyComponent().inject(this);
    }

    private void validateInputFields() {

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String error = UIUtils.isValid(getActivity(), UIUtils.EMAIL, charSequence);
                if (error.isEmpty()) {
                    isValidEmail = true;
                    firstName.setError(null);
                } else {
                    firstName.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        conformPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), NavigationActivity.class);
                        startActivity(intent);
                        break;
                    case ERROR:
                        Toast.makeText(getActivity(), "Registration failed.\n Please try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please accept 'Terms and Conditions'", Toast.LENGTH_SHORT).show();
        }
    }

    @OnEditorAction(R.id.et_confirm_password)
    public void imeSignUpButton() {
        signUp();
    }

    @OnClick(R.id.scroll_container)
    public void rootContainer() {
        UIUtils.hideKeyboard(getActivity());
    }


}
