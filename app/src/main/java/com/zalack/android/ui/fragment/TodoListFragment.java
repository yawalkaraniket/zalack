package com.zalack.android.ui.fragment;


import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

public class TodoListFragment extends BaseFragment {

    @BindView(R.id.not_tickets)
    TextView noTickets;

    @BindView(R.id.todo_list_recycler_view)
    RecyclerView todoListRecyclerView;

    @Inject
    ZalckPreferences prefs;

    private ProjectTicketsViewModel projectTicketsViewModel;
    private List<Ticket> todoList = new ArrayList<>();
    private TaskListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this, layout);

        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        todoListRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new TaskListAdapter(this.getContext());

        if (todoList.isEmpty()) {
            getAllTickets();
        } else {
            adapter.setTodoList(todoList);
            todoListRecyclerView.setAdapter(adapter);
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAllTickets() {

        projectTicketsViewModel = ViewModelProviders.of(this).get(ProjectTicketsViewModel.class);
        showProgress();
        projectTicketsViewModel.getProjectTickets(prefs.getToken(), 1).observe(getViewLifecycleOwner(), tickets -> {
            hideProgress();
            switch (tickets.getStatus()) {
                case SUCCESS:
                    for (Ticket ticket: tickets.getData().getData()) {
                        if (ticket.getStatus().equals("todo")) {
                            todoList.add(ticket);
                        }
                    }
                    if (todoList.isEmpty()) {
                     noTickets.setVisibility(View.VISIBLE);
                     todoListRecyclerView.setVisibility(View.GONE);
                    } else {
                        noTickets.setVisibility(View.GONE);
                        todoListRecyclerView.setVisibility(View.VISIBLE);
                        adapter.setTodoList(todoList);
                        todoListRecyclerView.setAdapter(adapter);
                    }
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public TodoListFragment() {
        // Required empty public constructor
    }

}
