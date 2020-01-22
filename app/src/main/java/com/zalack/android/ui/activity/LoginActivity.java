package com.zalack.android.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zalack.android.R;
import com.zalack.android.data.room.UserDatabase;
import com.zalack.android.data.room.table.User;
import com.zalack.android.data.room.viewmodel.UserViewModel;
import com.zalack.android.ui.common.FontEditText;
import com.zalack.android.ui.common.FontTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.emailId)
    FontEditText emailId;

    @BindView(R.id.password)
    FontEditText password;

    @BindView(R.id.sign_in_button)
    FontTextView signInButton;

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        userViewModel.create(emailId.getText().toString(), password.getText().toString());
        userViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.e("Database", users.get(0).getId() + users.get(0).getPassword());

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userViewModel.clear();
    }
}
