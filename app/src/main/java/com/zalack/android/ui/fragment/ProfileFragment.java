package com.zalack.android.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.ui.activity.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.header_title)
    TextView title;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_email)
    TextView userEmail;

    @BindView(R.id.user_mobile_number)
    TextView mobileNumber;

    @BindView(R.id.update_profile)
    LinearLayout updateProfile;

    @BindView(R.id.change_password)
    LinearLayout changePassword;

    @BindView(R.id.logout)
    LinearLayout logOut;

    @Inject
    ZalckPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, relativeLayout);
        setUpProgress(relativeLayout, this.getActivity());

        ((ZalckApp)this.getContext().getApplicationContext()).getMyComponent().inject(this);

        setProfileData();

        return relativeLayout;
    }

    private void setProfileData() {

        // Set name, email and mobile number.
        userName.setText(prefs.getUserName());
        userEmail.setText(prefs.getEmail());
        mobileNumber.setText(prefs.getNumber());

        // Set the profile image
        // int color = generator.getColor(getItem(position));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        // Get initials letters from the user name,
        String name = userName.getText().toString();
        String initialLetters = "";
        if (name.contains(" ")) {
            int index = name.indexOf(" ") + 1;
            initialLetters = userName.getText().charAt(0) + name.substring(index, index + 1);
        } else {
            initialLetters = String.valueOf(userName.getText().charAt(0));
        }

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(initialLetters.toUpperCase(), color); // radius

        profileImage.setImageDrawable(drawable);
    }

    @OnClick(R.id.logout)
    public void logOut() {
        prefs.setToken(null);
        this.getActivity().finish();
        Intent intent = new Intent(this.getActivity(), LoginActivity.class);
        this.getActivity().startActivity(intent);
    }

    public ProfileFragment() {

    }

}
