package com.zalack.android.ui.fragment;

import android.annotation.SuppressLint;
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
import com.zalack.android.data.models.all_projects.ProjectData;
import com.zalack.android.data.models.edit_project.EditProjectDetails;
import com.zalack.android.data.models.edit_project.ProjectDetails;
import com.zalack.android.data.webservice.viewmodel.EditProjectViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.ui.common.FontTextView;
import com.zalack.android.utils.UIUtils;

import java.io.Serializable;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.internal.ViewUtils.dpToPx;


public class EditProjectFragment extends BaseFragment {

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

    @BindView(R.id.header_title)
    FontTextView headerTitle;

    @Inject
    ZalckPreferences prefs;

    private EditProjectViewModel editProjectViewModel;
    private HashMap<String, String> userDetails = new HashMap<>();
    private ProjectData projectData;
    private ProjectDetails projectDetails;
    private EditProjectDetails editProjectDetails;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout  = (ConstraintLayout) inflater.inflate(R.layout.fragment_edit_project, container, false);
        ButterKnife.bind(this, layout);

        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        Bundle bundle = this.getArguments();
        Serializable applianceHomeInfo = bundle.getSerializable("project_data");
        if(applianceHomeInfo!=null){
            projectData = (ProjectData) applianceHomeInfo;
        }

        globalLayoutListener = () -> {
            if (getActivity()!=null) {
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

        headerTitle.setText("Edit Project " + projectData.getName());

        return layout;
    }

    @OnClick(R.id.btn_submit)
    void submit() {

        userDetails.put("name", projectName.getText().toString());
        userDetails.put("link", projectLink.getText().toString());
        UIUtils.hideKeyboard(getActivity());
        showProgress();
        editProjectViewModel = ViewModelProviders.of(this).get(EditProjectViewModel.class);
        editProjectViewModel.editProject(prefs.getToken(), projectData.getId(), userDetails).observe(getViewLifecycleOwner(), response->{
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    projectDetails = response.getData().getData();
                    editProjectDetails  = response.getData();
                    Toast.makeText(this.getContext(), editProjectDetails.getMessage(), Toast.LENGTH_SHORT).show();
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @OnClick(R.id.btn_clear)
    void cancel() {
        projectName.setText("");
        projectLink.setText("");
    }

    @OnClick(R.id.back_navigation)
    void navigateBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        root.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }

}
