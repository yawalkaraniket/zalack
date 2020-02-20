package com.zalack.android.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zalack.android.R;
import com.zalack.android.data.models.project_tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TodoListAdapterViewHolder> {

    private Context context;
    private List<Ticket> todoList = new ArrayList<>();

    public TaskListAdapter(Context context) {
        this.context = context;
    }

    public void setTodoList(List<Ticket> list) {
        if (this.todoList!=null) {
            this.todoList.clear();
        }
        this.todoList = list;
    }

    @NonNull
    @Override
    public TodoListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.todo_list_item_view, parent, false);

        return new TodoListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapterViewHolder holder, int position) {

        holder.taskName.setText(todoList.get(position).getName());
        holder.taskDescription.setText(todoList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoListAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView taskName;
        TextView taskDescription;

        TodoListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_description);
        }
    }
}
