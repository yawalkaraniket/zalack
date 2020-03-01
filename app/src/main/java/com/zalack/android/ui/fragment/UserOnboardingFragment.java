package com.zalack.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.webservice.viewmodel.SignUpViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.ui.activity.SignUpActivity;
import com.zalack.android.ui.common.CustomInputEditText;
import com.zalack.android.ui.common.FontTextView;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class UserOnboardingFragment extends BaseFragment {

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

    @BindView(R.id.btn_signUp)
    Button signUpButton;

    @BindView(R.id.name_layout)
    LinearLayout nameLayout;

    @BindView(R.id.email_layout)
    LinearLayout emailLayout;

    @BindView(R.id.password_layout)
    LinearLayout passwordLayout;

    @BindView(R.id.mobile_layout)
    LinearLayout mobileLayout;

    @BindView(R.id.accept_termas_and_condition)
    AppCompatCheckBox checkBox;

    @BindView(R.id.screen_title)
    FontTextView screenTitle;

    @BindView(R.id.navigate_back)
    ImageView backNavigation;

    @Inject
    ZalckPreferences prefs;

    private SignUpViewModel signUpViewModel;
    private String currentScreen = "";
    public static final String SCREEN = "screen", TYPE_ADD_NAME = "Add your name", TYPE_ADD_EMAIL = "Add your email";
    public static final String TYPE_MOBILE_NUMBER = "Verify mobile number", TYPE_ADD_PASSWORD = "Add your password";
    private boolean isValidFirstName, isValidLastName, isValidEmail, isValidMobileNumber, isValidPassword;
    private static HashMap<Credentials, String> credentials = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_user_onboarding, container, false);

        ButterKnife.bind(this, layout);
        setUpProgress(layout, this.getActivity());

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        currentScreen = getArguments().getString(SCREEN);

        setUpLayout();

        validateInputFields();

        return layout;
    }

    private void setUpLayout() {
        resetAllLayouts();
        switch (currentScreen) {
            case TYPE_ADD_NAME:
                nameLayout.setVisibility(View.VISIBLE);
                signUpButton.setText(getResources().getText(R.string.next));
                screenTitle.setText(TYPE_ADD_NAME);
                break;
            case TYPE_ADD_EMAIL:
                emailLayout.setVisibility(View.VISIBLE);
                signUpButton.setText(getResources().getText(R.string.next));
                screenTitle.setText(TYPE_ADD_EMAIL);
                backNavigation.setVisibility(View.VISIBLE);
                backNavigation.setOnClickListener(click->{
                    ((SignUpActivity)getActivity()).onBackPressed();
                });
                break;
            case TYPE_MOBILE_NUMBER:
                mobileLayout.setVisibility(View.VISIBLE);
                signUpButton.setText(getResources().getText(R.string.next));
                screenTitle.setText(TYPE_MOBILE_NUMBER);
                backNavigation.setVisibility(View.VISIBLE);
                backNavigation.setOnClickListener(click->{
                    ((SignUpActivity)getActivity()).onBackPressed();
                });
                break;
            case TYPE_ADD_PASSWORD:
                passwordLayout.setVisibility(View.VISIBLE);
                signUpButton.setText(getResources().getText(R.string.sign_up));
                screenTitle.setText(TYPE_ADD_PASSWORD);
                backNavigation.setVisibility(View.VISIBLE);
                backNavigation.setOnClickListener(click->{
                    ((SignUpActivity)getActivity()).onBackPressed();
                });
                break;
        }
    }

    private void resetAllLayouts() {
        nameLayout.setVisibility(View.GONE);
        emailLayout.setVisibility(View.GONE);
        mobileLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);
    }

    private void validateInputFields() {

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String error = UIUtils.isValid(getActivity(), UIUtils.NAME, charSequence);
                if (error.isEmpty()) {
                    isValidFirstName = true;
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    tilFirstName.setError(null);
                    credentials.put(Credentials.FIRST_NAME, charSequence.toString());
                } else {
                    isValidFirstName = false;
                    tilFirstName.setError(error);
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));

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

                String error = UIUtils.isValid(getActivity(), UIUtils.LAST_NAME, charSequence);
                if (error.isEmpty()) {
                    isValidLastName = true;
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    credentials.put(Credentials.LAST_NAME, charSequence.toString());
                    tilLastName.setError(null);
                } else {
                    isValidLastName= false;
                    tilLastName.setError(error);
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));

                }
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
                String error = UIUtils.isValid(getActivity(), UIUtils.EMAIL, charSequence);
                if (error.isEmpty()) {
                    isValidEmail = true;
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));

                    credentials.put(Credentials.EMAIL, charSequence.toString());
                    tilEmail.setError(null);
                } else {
                    isValidEmail = false;
                    tilEmail.setError(error);
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));

                }
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

                String error = UIUtils.isValid(getActivity(), UIUtils.MOBILE, charSequence);
                if (error.isEmpty()) {
                    isValidMobileNumber = true;
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    credentials.put(Credentials.MOBILE_NUMBER, charSequence.toString());
                    tilMobile.setError(null);
                } else {
                    isValidMobileNumber = false;
                    tilMobile.setError(error);
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));

                }
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
                String error = UIUtils.isValid(getActivity(), UIUtils.PASSWORD, charSequence);
                if (error.isEmpty()) {
                    isValidPassword = true;
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    credentials.put(Credentials.PASSWORD, charSequence.toString());
                    tilPassword.setError(null);
                } else {
                    isValidPassword = false;
                    tilPassword.setError(error);
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));

                }
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
                String error = UIUtils.isValid(getActivity(), UIUtils.PASSWORD, charSequence);
                if (error.isEmpty() && !Objects.requireNonNull(credentials.get(Credentials.PASSWORD)).isEmpty()) {
                    if (Objects.equals(credentials.get(Credentials.PASSWORD), charSequence.toString())) {
                        isValidPassword = true;
                        credentials.put(Credentials.PASSWORD, charSequence.toString());
                        tilConfirmPassword.setError(null);
                        signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_button_50));
                    } else {
                        tilConfirmPassword.setError("Both password should match.");
                        signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));
                    }
                } else {
                    isValidPassword = false;
                    tilConfirmPassword.setError(error);
                    signUpButton.setBackground(getResources().getDrawable(R.drawable.circular_disable_bg_50));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.btn_signUp)
    public void signUp() {
        switch (currentScreen) {
            case TYPE_ADD_NAME:
                if (isValidFirstName && isValidLastName) {
                    ((SignUpActivity) getActivity()).openAddEmailScreen();
                }
                break;
            case TYPE_ADD_EMAIL:
                if (isValidEmail) {
                    ((SignUpActivity) getActivity()).openAddMobileScreen();
                }
                break;
            case TYPE_MOBILE_NUMBER:
                if (isValidMobileNumber) {
                    ((SignUpActivity) getActivity()).openAddPasswordScreen();
                }
                break;
            case TYPE_ADD_PASSWORD:
                if (isValidPassword) {

                    if (checkBox.isChecked()) {
                        HashMap<String, String> user = new HashMap<>();
                        user.put("email", credentials.get(Credentials.EMAIL));
                        user.put("password", credentials.get(Credentials.PASSWORD));
                        user.put("first_name", credentials.get(Credentials.FIRST_NAME));
                        user.put("last_name", credentials.get(Credentials.LAST_NAME));
                        user.put("confirmed", credentials.get(Credentials.PASSWORD));
                        user.put("mobile", credentials.get(Credentials.MOBILE_NUMBER));

                        showProgress();
                        signUpViewModel.registerUser(user).observe(this, response -> {
                            hideProgress();
                            SignUp data = response.getData();
                            switch (response.getStatus()) {
                                case SUCCESS:
                                    prefs.setToken(data.getTokenType().substring(0, 1).toUpperCase()
                                            + data.getTokenType().substring(1) + " " + data.getAccessToken());
                                    prefs.setName(credentials.get(Credentials.FIRST_NAME) + " " + credentials.get(Credentials.LAST_NAME));
                                    prefs.setEmail(credentials.get(Credentials.EMAIL));
                                    prefs.setMobileNumber(credentials.get(Credentials.MOBILE_NUMBER));
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

    private enum Credentials {
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        MOBILE_NUMBER,
        PASSWORD
    }
}
