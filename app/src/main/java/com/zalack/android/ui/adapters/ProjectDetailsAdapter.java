package com.zalack.android.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zalack.android.R;
import com.zalack.android.data.models.all_projects.ProjectData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectDetailsAdapter extends RecyclerView.Adapter<ProjectDetailsAdapter.ProjectDetailsAdapterViewHolder> {

    private Context context;
    private List<ProjectData> projects = new ArrayList<>();
    private OnProjectClickListener listener;

    public ProjectDetailsAdapter(Context context, OnProjectClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setList(List<ProjectData> list) {
        if (this.projects!=null) {
            this.projects.clear();
        }
        this.projects = list;
    }

    @NonNull
    @Override
    public ProjectDetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.project_details_item_layout, parent, false);

        return new ProjectDetailsAdapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectDetailsAdapterViewHolder holder, int position) {
        holder.projectName.setText(projects.get(position).getName());
        holder.projectLink.setText(projects.get(position).getLink());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class ProjectDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView projectName, projectLink;
        CircleImageView deleteProject;
        CircleImageView editProject;
        OnProjectClickListener listener;

        ProjectDetailsAdapterViewHolder(@NonNull View itemView, OnProjectClickListener listener) {
            super(itemView);

            projectName = itemView.findViewById(R.id.project_name);
            projectLink = itemView.findViewById(R.id.project_link);
            deleteProject = itemView.findViewById(R.id.delete_project);
            editProject = itemView.findViewById(R.id.edit_project);

            this.listener = listener;
            deleteProject.setOnClickListener(this);
            editProject.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface OnProjectClickListener {
        void onItemClick(View view, int position);
    }
}
