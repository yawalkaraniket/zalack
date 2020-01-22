package com.zalack.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.zalack.android.R;
import com.zalack.android.data.webservice.viewmodel.MoviesViewModel;
import com.zalack.android.ui.common.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activityMain_btn_Register)
    FontTextView registerButton;

    @BindView(R.id.activityMain_btn_SignIn)
    FontTextView signInButton;

    MoviesViewModel moviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

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


        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moviesViewModel.clear();
    }
}
