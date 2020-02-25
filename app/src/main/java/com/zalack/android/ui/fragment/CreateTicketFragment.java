package com.zalack.android.ui.fragment;

import android.content.Intent;
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

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.webservice.viewmodel.CreateTicketViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.utils.UIUtils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.internal.ViewUtils.dpToPx;

public class CreateTicketFragment extends BaseFragment {

    @BindView(R.id.ticket_name)
    EditText ticketName;

    @BindView(R.id.ticket_description)
    EditText ticketDescription;

    @BindView(R.id.ticket_status)
    MaterialSpinner ticketStatus;

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

    private HashMap<String, String> userDetails = new HashMap<>();
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private CreateTicketViewModel createTicketViewModel;
    String selectedStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_create_ticket, container, false);

        ButterKnife.bind(this, layout);
        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        globalLayoutListener = () -> {
            if (getActivity()!=null && getActivity() instanceof NavigationActivity) {
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

        ticketStatus.setItems(getResources().getStringArray(R.array.ticket_status));
        ticketStatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selectedStatus = item;
            }
        });

        return layout;
    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        userDetails.put("name", ticketName.getText().toString());
        userDetails.put("description", ticketDescription.getText().toString());
        userDetails.put("project_id", String.valueOf(prefs.getCurrentProjectId()));
        userDetails.put("status", String.valueOf(selectedStatus).toLowerCase());
        UIUtils.hideKeyboard(this.getActivity());
        showProgress();
        createTicketViewModel = ViewModelProviders.of(this).get(CreateTicketViewModel.class);
        createTicketViewModel.createTicket(prefs.getToken(), userDetails).observe(getViewLifecycleOwner(), response ->{
            hideProgress();
            switch (response.getStatus()) {
                case SUCCESS:
                    this.getActivity().sendBroadcast(new Intent("task_status_changed"));
                    navigateBack();
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @OnClick(R.id.btn_clear)
    public void cancel() {
        ticketName.setText("");
        ticketDescription.setText("");
    }

    @OnClick(R.id.back_navigation)
    public void navigateBack() {
        ((NavigationActivity)getActivity()).navigateToTaskFragment();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        root.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }
}
