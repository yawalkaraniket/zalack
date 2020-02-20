package com.zalack.android.ui.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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

public class InProgressListFragment extends BaseFragment {

    @BindView(R.id.not_tickets)
    TextView noTicketsView;

    @BindView(R.id.in_progress_tickets)
    RecyclerView inProgressTickets;

    @Inject
    ZalckPreferences prefs;

    private ProjectTicketsViewModel projectTicketsViewModel;
    private List<Ticket> inProgressList = new ArrayList<>();
    private TaskListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_in_progress_list, container, false);
        ButterKnife.bind(this,layout);

        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        inProgressTickets.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new TaskListAdapter(this.getContext());

        if (inProgressList.isEmpty()) {
            getAllTickets();
        } else {
            adapter.setTodoList(inProgressList);
            inProgressTickets.setAdapter(adapter);
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
                            inProgressList.add(ticket);
                        }
                    }
                    if (inProgressList.isEmpty()) {
                        noTicketsView.setVisibility(View.VISIBLE);
                        inProgressTickets.setVisibility(View.GONE);
                    } else {
                        noTicketsView.setVisibility(View.GONE);
                        inProgressTickets.setVisibility(View.VISIBLE);
                        adapter.setTodoList(inProgressList);
                        inProgressTickets.setAdapter(adapter);
                    }
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public InProgressListFragment() {
        // Required empty public constructor
    }

}
