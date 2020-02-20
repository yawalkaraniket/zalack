package com.zalack.android.dagger.component;

import com.zalack.android.dagger.module.SharedPreferencesModule;
import com.zalack.android.dagger.scope.MyApplicationScope;
import com.zalack.android.ui.activity.LauncherActivity;
import com.zalack.android.ui.activity.LoginActivity;
import com.zalack.android.ui.fragment.DoneListFragment;
import com.zalack.android.ui.fragment.InProgressListFragment;
import com.zalack.android.ui.fragment.ProfileFragment;
import com.zalack.android.ui.fragment.ProjectsFragment;
import com.zalack.android.ui.fragment.TodoListFragment;

import dagger.Component;

@Component(modules = SharedPreferencesModule.class)
@MyApplicationScope
public interface PreferenceComponent {
    void inject(LoginActivity loginActivity);
    void inject(LauncherActivity launcherActivity);
    void inject(ProfileFragment profileFragment);
    void inject(ProjectsFragment projectsFragment);
    void inject(TodoListFragment todoListFragment);
    void inject(InProgressListFragment todoListFragment);
    void inject(DoneListFragment doneListFragment);
}
