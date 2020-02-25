package com.zalack.android.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zalack.android.R;
import com.zalack.android.data.models.project_tickets.Ticket;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TodoListAdapterViewHolder> {

    private Context context;
    private List<Ticket> todoList;
    private OnItemClickListener listener;
    public static final String TODO = "Todo";
    public static final String IN_PROGRESS = "In Progress";
    public static final String DONE = "Done";
    String type;

    public TaskListAdapter(Context context, OnItemClickListener listener, String type) {
        this.context = context;
        this.listener = listener;
        this.type = type;
    }

    public void clearList() {
        if (this.todoList != null) {
            this.todoList.clear();
            notifyDataSetChanged();
        }
    }

    public void setTodoList(List<Ticket> list) {

        this.todoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.todo_list_item_view, parent, false);

        return new TodoListAdapterViewHolder(view, listener);
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

    class TodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, editTaskButton, updateStatusMenuIcon;
        TextView taskName, taskDescription;
        OnItemClickListener listener;

        TodoListAdapterViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_description);
            editTaskButton = itemView.findViewById(R.id.edit_task_button);
            this.listener = listener;

            updateStatusMenuIcon = itemView.findViewById(R.id.update_status_menu_icon);
            updateStatusMenuIcon.setOnClickListener(view -> {

                PopupMenu menu = new PopupMenu(context, updateStatusMenuIcon);
                switch (type) {
                    case TODO:
                        menu.inflate(R.menu.task_todo_menus);
                        break;
                    case IN_PROGRESS:
                        menu.inflate(R.menu.task_inprogress_menu);
                        break;
                    case DONE:
                        menu.inflate(R.menu.task_done_menus);
                        break;
                }
                menu.setOnMenuItemClickListener(menuItem -> {
                    this.listener.onItemClick(menuItem.getItemId(), getAdapterPosition());

                    return true;
                });
                menu.show();
            });
            editTaskButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick(int id, int position);
    }
}
