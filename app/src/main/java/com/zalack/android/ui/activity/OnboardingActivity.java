package com.zalack.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Placeholder;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.zalack.android.R;
import com.zalack.android.ui.common.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.start)
    FontTextView getStarted;

    @BindView(R.id.rootLayout)
    ConstraintLayout onboardingContainer;

    @BindView(R.id.maleImage)
    ImageView maleImage;

    @BindView(R.id.femaleImage)
    ImageView femaleView;

    @BindView(R.id.otherImage)
    ImageView otherView;

    @BindView(R.id.selectedImagePlaceHolder)
    Placeholder selectedImagePlaceHolder;

    @BindView(R.id.gender_groups)
    Group selectGenderGroups;

    @BindView(R.id.title)
    FontTextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ButterKnife.bind(this);

        initListeners();
    }

    private void initListeners() {
        selectedImagePlaceHolder.setContentId(maleImage.getId());
        maleImage.setOnClickListener(this);
        femaleView.setOnClickListener(this);
        otherView.setOnClickListener(this);
    }

    @OnClick(R.id.start)
    public void setGetStarted() {
        selectGenderGroups.setVisibility(View.GONE);
        onboardingContainer.removeView(selectedImagePlaceHolder);
        ConstraintSet set = new ConstraintSet();
        set.clone(OnboardingActivity.this, R.layout.animation_registration_layout);
        set.applyTo(onboardingContainer);
        Transition transition = new ChangeBounds();
        transition.setInterpolator(new OvershootInterpolator());
        transition.setDuration(2000);
        TransitionManager.beginDelayedTransition(onboardingContainer, transition);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()!= getStarted.getId()) {
            TransitionManager.beginDelayedTransition(onboardingContainer);
            selectedImagePlaceHolder.setContentId(view.getId());
        }
    }
}
