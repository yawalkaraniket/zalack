package com.zalack.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.project_tickets.Ticket;
import com.zalack.android.data.webservice.viewmodel.ProjectTicketsViewModel;
import com.zalack.android.ui.activity.UpdateProfileActivity;
import com.zalack.android.ui.adapters.TaskListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoneListFragment extends BaseFragment implements TaskListAdapter.OnItemClickListener {

    @BindView(R.id.not_tickets)
    TextView noTicketsView;

    @BindView(R.id.done_tickets)
    RecyclerView doneTicketsRecyclerView;

    @Inject
    ZalckPreferences prefs;

    private ProjectTicketsViewModel projectTicketsViewModel;
    private List<Ticket> doneTicketsList = new ArrayList<>();
    private TaskListAdapter taskListAdapter;
    private boolean isVisible = false;
    private AlertDialog.Builder builder;
    private AlertDialog alert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_done_list, container, false);
        ButterKnife.bind(this,layout);

        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        doneTicketsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        taskListAdapter = new TaskListAdapter(this.getContext(), this, TaskListAdapter.DONE);

        if (doneTicketsList.isEmpty()) {
            getAllTickets();
        } else {
            taskListAdapter.setTodoList(doneTicketsList);
            doneTicketsRecyclerView.setAdapter(taskListAdapter);
        }

        return layout;
    }

    private void getAllTickets() {

        projectTicketsViewModel = ViewModelProviders.of(this).get(ProjectTicketsViewModel.class);
        showProgress();
        projectTicketsViewModel.getProjectTickets(prefs.getToken(), prefs.getCurrentProjectId()).observe(getViewLifecycleOwner(), tickets -> {
            hideProgress();
            switch (tickets.getStatus()) {
                case SUCCESS:
                    for (Ticket ticket: tickets.getData().getData()) {
                        if (ticket.getStatus().equals("completed")) {
                            doneTicketsList.add(ticket);
                        }
                    }
                    if (doneTicketsList.isEmpty()) {
                        noTicketsView.setVisibility(View.VISIBLE);
                        doneTicketsRecyclerView.setVisibility(View.GONE);
                    } else {
                        noTicketsView.setVisibility(View.GONE);
                        doneTicketsRecyclerView.setVisibility(View.VISIBLE);
                        taskListAdapter.setTodoList(doneTicketsList);
                        doneTicketsRecyclerView.setAdapter(taskListAdapter);
                    }
                    break;
                case ERROR:
                    noTicketsView.setVisibility(View.VISIBLE);
                    doneTicketsRecyclerView.setVisibility(View.GONE);
                    break;
            }
        });
    }

    public DoneListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(View view, int position) {

        if (view != null && position >= 0) {
            switch (view.getId()) {
                case R.id.edit_task_button:
                    Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                    intent.putExtra(UpdateProfileActivity.SCREEN, UpdateProfileActivity.TYPE_UPDATE_TICKET);
                    intent.putExtra("status", doneTicketsList.get(position).getStatus());
                    intent.putExtra("id", String.valueOf(doneTicketsList.get(position).getId()));
                    intent.putExtra("ticket", doneTicketsList.get(position));
                    getActivity().startActivity(intent);
                    break;
            }
        }

    }

    @Override
    public void onItemClick(int id, int position) {

        switch (id) {
            case R.id.todo_button:
                updateStatus("todo",position);
                break;
            case R.id.inprogress_button:
                updateStatus("in_progress", position);
                break;
            case R.id.done_button:
                updateStatus("completed", position);
                break;
            case R.id.delete_task:
                deleteTask(position);
                break;
        }

    }

    private void deleteTask(int position) {

        builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Do you want to delete this ticket ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    showProgress();
                    projectTicketsViewModel.deleteProject(prefs.getToken(), doneTicketsList.get(position).getId()).observe(getViewLifecycleOwner(), response -> {
                        switch (response.getStatus()) {
                            case SUCCESS:
                                Toast.makeText(this.getContext(), response.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                doneTicketsList.clear();
                                taskListAdapter.clearList();
                                this.getActivity().sendBroadcast(new Intent("task_status_changed"));
                                break;
                            case ERROR:
                                Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                });
        //Creating dialog box
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        IntentFilter filter = new IntentFilter();
        filter.addAction("task_status_changed");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getActivity() != null && broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("task_status_changed")) {
                    if (projectTicketsViewModel != null) {
                        taskListAdapter.clearList();
                            getAllTickets();
                    }
                }
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
    }

    private void updateStatus(String statusType, int position) {
        HashMap<String, String> updatedValues = new HashMap<>();
        updatedValues.put("name", doneTicketsList.get(position).getName());
        updatedValues.put("status", statusType);
        updatedValues.put("description", doneTicketsList.get(position).getName());
        showProgress();
        projectTicketsViewModel.updateTickets(prefs.getToken(), doneTicketsList.get(position).getId(), updatedValues)
                .observe(getViewLifecycleOwner(), response -> {
                    hideProgress();
                    switch (response.getStatus()) {
                        case SUCCESS:
                            doneTicketsList.clear();
                            this.getActivity().sendBroadcast(new Intent("task_status_changed"));
                            break;
                        case ERROR:
                            Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

}
