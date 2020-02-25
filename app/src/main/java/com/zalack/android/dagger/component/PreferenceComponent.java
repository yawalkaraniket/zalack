package com.zalack.android.dagger.component;

import com.zalack.android.dagger.module.SharedPreferencesModule;
import com.zalack.android.dagger.scope.MyApplicationScope;
import com.zalack.android.data.webservice.viewmodel.SignUpViewModel;
import com.zalack.android.ui.activity.LauncherActivity;
import com.zalack.android.ui.activity.LoginActivity;
import com.zalack.android.ui.activity.SignUpActivity;
import com.zalack.android.ui.activity.UpdateProfileActivity;
import com.zalack.android.ui.fragment.AddProjectFragment;
import com.zalack.android.ui.fragment.CreateTicketFragment;
import com.zalack.android.ui.fragment.DoneListFragment;
import com.zalack.android.ui.fragment.EditProjectFragment;
import com.zalack.android.ui.fragment.InProgressListFragment;
import com.zalack.android.ui.fragment.ProfileFragment;
import com.zalack.android.ui.fragment.ProjectsFragment;
import com.zalack.android.ui.fragment.TasksFragment;
import com.zalack.android.ui.fragment.TodoListFragment;

import dagger.Component;

@Component(modules = SharedPreferencesModule.class)
@MyApplicationScope
public interface PreferenceComponent {
    void inject(LoginActivity loginActivity);
    void inject(UpdateProfileActivity updateProfileActivity);
    void inject(LauncherActivity launcherActivity);
    void inject(ProfileFragment profileFragment);
    void inject(ProjectsFragment projectsFragment);
    void inject(TodoListFragment todoListFragment);
    void inject(InProgressListFragment todoListFragment);
    void inject(DoneListFragment doneListFragment);
    void inject(SignUpActivity signUpActivity);
    void inject(AddProjectFragment addProjectFragment);
    void inject(EditProjectFragment editProjectFragment);
    void inject(CreateTicketFragment createTicketFragment);
    void inject(TasksFragment tasksFragment);
}
