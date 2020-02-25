package com.zalack.android.ui.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.models.add_project.AddedProjectData;
import com.zalack.android.data.webservice.viewmodel.AddProjectViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.internal.ViewUtils.dpToPx;

public class AddProjectFragment extends BaseFragment {

    @BindView(R.id.project_name)
    EditText projectName;

    @BindView(R.id.project_link)
    EditText projectLink;

    @BindView(R.id.btn_clear)
    Button cancelButton;

    @BindView(R.id.btn_submit)
    Button submitButton;

    @BindView(R.id.back_navigation)
    ImageView backNavigation;

    @BindView(R.id.rootLayout)
    ConstraintLayout root;

    @Inject
    ZalckPreferences prefs;

    private AddProjectViewModel addProjectViewModel;
    AddedProjectData addedProjectData = null;
    AddedProject addedProject = null;
    private HashMap<String, String> userDetails = new HashMap<>();

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_add_project, container, false);
        ButterKnife.bind(this, layout);
        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        addProjectViewModel = ViewModelProviders.of(this).get(AddProjectViewModel.class);
        globalLayoutListener = () -> {
            if (getActivity()!=null && getActivity() instanceof NavigationActivity ) {
                int heightDiff = root.getRootView().getHeight() - root.getHeight();
                if (heightDiff > dpToPx(getActivity(), 200)) {
                    // If more than 200 dp, it's probably a keyboard...
                    ((NavigationActivity) getActivity()).hideNavigation();
                } else {
                    ((NavigationActivity) getActivity()).showNavigation();
                }
            }
        };
        root.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);


        return layout;
    }

    @OnClick(R.id.btn_submit)
    public void submit() {

        userDetails.put("name", projectName.getText().toString());
        userDetails.put("link", projectLink.getText().toString());
        UIUtils.hideKeyboard(getActivity());
        showProgress();
        addProjectViewModel.addProject(prefs.getToken(), userDetails).observe(getViewLifecycleOwner(), response -> {
        hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    addedProjectData = response.getData().getData();
                    addedProject  = response.getData();
                    Toast.makeText(this.getContext(), addedProject.getMessage(), Toast.LENGTH_SHORT).show();
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @OnClick(R.id.btn_clear)
    public void cancel() {
        projectName.setText("");
        projectLink.setText("");
    }

    @OnClick(R.id.back_navigation)
    public void navigateBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        root.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }
}
