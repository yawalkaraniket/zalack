package com.zalack.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.models.add_project.AddedProjectData;
import com.zalack.android.data.models.all_projects.ProjectData;
import com.zalack.android.data.models.project_tickets.Ticket;
import com.zalack.android.data.webservice.viewmodel.AddProjectViewModel;
import com.zalack.android.data.webservice.viewmodel.CreateTicketViewModel;
import com.zalack.android.data.webservice.viewmodel.EditProjectViewModel;
import com.zalack.android.data.webservice.viewmodel.ProjectTicketsViewModel;
import com.zalack.android.data.webservice.viewmodel.UpdateProfileUserModel;
import com.zalack.android.ui.common.FontEditText;
import com.zalack.android.ui.common.FontTextView;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class UpdateProfileActivity extends BaseActivity {

    public final static String TYPE_CHANGE_PASSWORD = "Change Password";
    public final static String TYPE_UPDATE_PROFILE = "Update Profile";
    public final static String TYPE_ADD_PROJECT = "Add project";
    public final static String TYPE_UPDATE_PROJECT = "Update Project";
    public final static String TYPE_UPDATE_TICKET = "Edit Ticket";
    public final static String TYPE_CREATE_NEW_TICKET = "Create New Ticket";
    public final static String SCREEN = "screen";
    private String buttonType = "Add";

    @BindView(R.id.back_navigation)
    ImageView navigateBack;

    @BindView(R.id.header_title)
    FontTextView title;

    @BindView(R.id.rootLayout)
    ConstraintLayout rootLayout;

    @BindView(R.id.btn_update)
    Button updateButton;

    // Change password layout

    @BindView(R.id.change_password_layout)
    LinearLayout changePasswordLayout;

    @BindView(R.id.et_first_name)
    FontEditText firstName;

    @BindView(R.id.et_last_name)
    FontEditText lastName;

    // Update profile layout

    @BindView(R.id.update_profile_layout)
    LinearLayout updateProfileLayout;

    @BindView(R.id.current_password)
    FontEditText currentPassword;

    @BindView(R.id.new_password)
    FontEditText newPassword;

    @BindView(R.id.new_password_confirm)
    FontEditText confirmPassword;

    // Add project layout

    @BindView(R.id.add_project_layout)
    LinearLayout addProjectLayout;

    @BindView(R.id.project_name)
    FontEditText projectName;

    @BindView(R.id.project_link)
    FontEditText projectLink;

    // Edit ticket layout

    @BindView(R.id.edit_ticket_layout)
    LinearLayout editTicketLayout;

    @BindView(R.id.update_ticket_name)
    FontEditText updateTicketName;

    @BindView(R.id.update_ticket_description)
    FontEditText updateTicketDescription;

    // Create new ticket

    @BindView(R.id.add_ticket_layout)
    LinearLayout addNewTicketLayout;

    @BindView(R.id.ticket_name)
    FontEditText ticketName;

    @BindView(R.id.ticket_description)
    FontEditText ticketDescription;

    // Update project

    @BindView(R.id.update_project_layout)
    LinearLayout updateProjectLayout;

    @BindView(R.id.updated_project_name)
    FontEditText updatedProjectName;

    @BindView(R.id.updated_project_link)
    FontEditText updatedProjectLink;

    @Inject
    ZalckPreferences prefs;

    private UpdateProfileUserModel updateProfileUserModel;
    private AddProjectViewModel addProjectViewModel;
    private ProjectTicketsViewModel projectTicketsViewModel;
    private CreateTicketViewModel createTicketViewModel;
    private EditProjectViewModel editProjectViewModel;

    Ticket ticket;
    ProjectData projectData;

    AddedProjectData addedProjectData = null;
    AddedProject addedProject = null;


    private String currentScreen;
    private HashMap<String, String> userDetails = new HashMap<>();
    View currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);

        setUpProgress(rootLayout);
        ((ZalckApp) this.getApplicationContext()).getMyComponent().inject(this);
        currentScreen = this.getIntent().getStringExtra(SCREEN);
        setUpUI();
        title.setText(currentScreen);

        updateProfileUserModel = ViewModelProviders.of(this).get(UpdateProfileUserModel.class);
        addProjectViewModel = ViewModelProviders.of(this).get(AddProjectViewModel.class);
        projectTicketsViewModel = ViewModelProviders.of(this).get(ProjectTicketsViewModel.class);

        currentLayout.setOnClickListener(click -> {
            rootClick();
        });
    }

    @OnClick(R.id.rootLayout)
    public void rootClick() {
        UIUtils.hideKeyboard(this);
    }

    @OnClick(R.id.back_navigation)
    public void navigateBack() {
        this.finish();
    }

    @OnClick(R.id.btn_clear)
    public void cancel() {
        clearAllFields();
    }

    @OnClick(R.id.btn_update)
    public void submit() {
        switch (currentScreen) {
            case TYPE_CHANGE_PASSWORD:
                changePassword();
                break;
            case TYPE_UPDATE_PROFILE:
                updateProfile();
                break;
            case TYPE_ADD_PROJECT:
                addNewProject();
                break;
            case TYPE_UPDATE_TICKET:
                updateTicket();
                break;
            case TYPE_CREATE_NEW_TICKET:
                createNewTicket();
                break;
            case TYPE_UPDATE_PROJECT:
                updateProject();
                break;
        }
    }

    @OnEditorAction(R.id.new_password_confirm)
    public void confirmPassword() {
        changePassword();
    }

    @OnEditorAction(R.id.updated_project_link)
    public void updateProjectAction() {
        updateProject();
    }

    @OnEditorAction(R.id.project_link)
    public void createProjectLinkAction() {
        addNewProject();
    }

    @OnEditorAction(R.id.update_ticket_description)
    public void updateTicketAction() {
        updateTicket();
    }

    @OnEditorAction(R.id.et_last_name)
    public void updateProfileAction() {
        updateProfile();
    }

    private void setUpUI() {
        switch (currentScreen) {
            case TYPE_CHANGE_PASSWORD:
                changePasswordLayout.setVisibility(View.VISIBLE);
                currentLayout = changePasswordLayout;
                buttonType = "Update";
                break;
            case TYPE_UPDATE_PROFILE:
                updateProfileLayout.setVisibility(View.VISIBLE);
                currentLayout = updateProfileLayout;
                firstName.setText(prefs.getName().substring(0, prefs.getName().indexOf(" ")));
                lastName.setText(prefs.getName().substring(prefs.getName().indexOf(" ") + 1));
                buttonType = "Update";
                break;
            case TYPE_ADD_PROJECT:
                addProjectLayout.setVisibility(View.VISIBLE);
                currentLayout = addProjectLayout;
                break;
            case TYPE_UPDATE_TICKET:
                editTicketLayout.setVisibility(View.VISIBLE);
                currentLayout = editTicketLayout;
                ticket = (Ticket) getIntent().getSerializableExtra("ticket");
                updateTicketName.setText(ticket.getName());
                updateTicketDescription.setText(ticket.getDescription());
                buttonType = "Update";
                break;
            case TYPE_CREATE_NEW_TICKET:
                addNewTicketLayout.setVisibility(View.VISIBLE);
                currentLayout = addNewTicketLayout;
                buttonType = "Create";
                break;
            case TYPE_UPDATE_PROJECT:
                updateProjectLayout.setVisibility(View.VISIBLE);
                currentLayout = updateProjectLayout;
                projectData = (ProjectData) getIntent().getSerializableExtra("project_data");
                updatedProjectName.setText(projectData.getName());
                updatedProjectLink.setText(projectData.getLink());
                title.setText(projectData.getName());
                buttonType = "Update";
                break;
        }

        updateButton.setText(buttonType);
    }

    private void updateProject() {
        userDetails.put("name", updatedProjectName.getText().toString());
        userDetails.put("link", updatedProjectLink.getText().toString());
        showProgress();
        editProjectViewModel = ViewModelProviders.of(this).get(EditProjectViewModel.class);
        editProjectViewModel.editProject(prefs.getToken(), projectData.getId(), userDetails).observe(this, response -> {
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    this.finish();
                    sendBroadcast(new Intent("task_status_changed"));
                    Toast.makeText(this, response.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this, "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void createNewTicket() {
        userDetails.clear();
        userDetails.put("name", ticketName.getText().toString());
        userDetails.put("description", ticketDescription.getText().toString());
        userDetails.put("project_id", String.valueOf(prefs.getCurrentProjectId()));
        userDetails.put("status", "todo");
        showProgress();
        createTicketViewModel = ViewModelProviders.of(this).get(CreateTicketViewModel.class);
        createTicketViewModel.createTicket(prefs.getToken(), userDetails).observe(this, response -> {
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    finish();
                    this.sendBroadcast(new Intent("task_status_changed"));
                    break;
                case ERROR:
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    private void updateTicket() {
        userDetails.put("name", updateTicketName.getText().toString());
        userDetails.put("status", ticket.getStatus());
        userDetails.put("description", updateTicketDescription.getText().toString());
        showProgress();
        projectTicketsViewModel.updateTickets(prefs.getToken(),
                Integer.parseInt(this.getIntent().getStringExtra("id")), userDetails)
                .observe(this, response -> {
                    hideProgress();
                    switch (response.getStatus()) {
                        case SUCCESS:
                            this.finish();
                            sendBroadcast(new Intent("task_status_changed"));
                            break;
                        case ERROR:
                            Toast.makeText(this, "Unable to load data", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });

    }

    private void addNewProject() {
        userDetails.put("name", projectName.getText().toString());
        userDetails.put("link", projectLink.getText().toString());
        UIUtils.hideKeyboard(this);
        showProgress();
        addProjectViewModel.addProject(prefs.getToken(), userDetails).observe(this, response -> {
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    addedProjectData = response.getData().getData();
                    addedProject = response.getData();
                    Toast.makeText(this, addedProject.getMessage(), Toast.LENGTH_SHORT).show();
                    sendBroadcast(new Intent("task_status_changed"));
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this, "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void updateProfile() {
        userDetails.put("first_name", firstName.getText().toString());
        userDetails.put("last_name", lastName.getText().toString());
        showProgress();
        updateProfileUserModel.updateUser(prefs.getToken(), userDetails).observe(this, response -> {
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    prefs.setName(response.getData().getData().getUser().getFirstName() + " " +
                            response.getData().getData().getUser().getLastName());
                    Toast.makeText(this, response.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    private void changePassword() {
        userDetails.put("current_password", currentPassword.getText().toString());
        userDetails.put("new_password", currentPassword.getText().toString());
        userDetails.put("password_confirmation", currentPassword.getText().toString());
        showProgress();
        updateProfileUserModel.changePassword(prefs.getToken(), userDetails).observe(this, response -> {
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    Toast.makeText(this, response.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void clearAllFields() {
        firstName.setText("");
        lastName.setText("");
        confirmPassword.setText("");
        currentPassword.setText("");
        newPassword.setText("");
        projectName.setText("");
        projectLink.setText("");
        updatedProjectLink.setText("");
        updatedProjectName.setText("");
        ticketName.setText("");
        ticketDescription.setText("");
        updateTicketName.setText("");
        updateTicketDescription.setText("");
    }
}
