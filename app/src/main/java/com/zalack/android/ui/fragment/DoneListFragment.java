package com.zalack.android.ui.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.project_tickets.Ticket;
import com.zalack.android.data.webservice.viewmodel.ProjectTicketsViewModel;
import com.zalack.android.ui.adapters.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoneListFragment extends BaseFragment {

    @BindView(R.id.not_tickets)
    TextView noTicketsView;

    @BindView(R.id.done_tickets)
    RecyclerView doneTicketsRecyclerView;

    @Inject
    ZalckPreferences prefs;

    private ProjectTicketsViewModel projectTicketsViewModel;
    private List<Ticket> doneTicketsList = new ArrayList<>();
    private TaskListAdapter taskListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_done_list, container, false);
        ButterKnife.bind(this,layout);

        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        doneTicketsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        taskListAdapter = new TaskListAdapter(this.getContext());

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
        projectTicketsViewModel.getProjectTickets(prefs.getToken(), 1).observe(getViewLifecycleOwner(), tickets -> {
            hideProgress();
            switch (tickets.getStatus()) {
                case SUCCESS:
                    for (Ticket ticket: tickets.getData().getData()) {
                        if (ticket.getStatus().equals("inprogress")) {
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
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public DoneListFragment() {
        // Required empty public constructor
    }

}
