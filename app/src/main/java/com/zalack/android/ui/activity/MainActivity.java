package com.zalack.android.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.zalack.android.R;
import com.zalack.android.data.webservice.viewmodel.MoviesViewModel;
import com.zalack.android.ui.common.FontTextView;
import com.zalack.android.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DroidListener {

    @BindView(R.id.activityMain_btn_Register)
    FontTextView registerButton;

    @BindView(R.id.activityMain_btn_SignIn)
    FontTextView signInButton;

    MoviesViewModel moviesViewModel;
    private DroidNet mDroidNet;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.activityMain_btn_SignIn)
    public void signIn() {

        moviesViewModel.getAllMovies().observe(this, movieResponses -> {
                    // All response comes hear.
                });

        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    @OnClick(R.id.activityMain_btn_Register)
    public void register() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moviesViewModel.clear();
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
