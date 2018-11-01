package com.broooapps.synctasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.broooapps.synctasks.R;
import com.broooapps.synctasks.models.datamodel.Task;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> {


    private ArrayList<Task> taskList = new ArrayList<>();
    private onClickListener listener;

    public TasksListAdapter(ArrayList<Task> tasksList, onClickListener listener) {
        this.taskList = tasksList;
        this.listener = listener;
    }

    public void updateData(ArrayList<Task> newList) {
        this.taskList.clear();
        this.taskList.addAll(newList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.linear_task_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Task task = taskList.get(i);
        viewHolder.checkBox.setChecked(task.isStatus());
        viewHolder.text_task.setText(task.getTask_name());
        viewHolder.timestamp_task.setText(task.getTimestamp());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.onCheckClick(task, i, b);
            }
        });
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(task, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (taskList == null) return 0;
        return taskList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView text_task, timestamp_task;
        CardView container;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_task);
            container = itemView.findViewById(R.id.container);
            text_task = itemView.findViewById(R.id.text_task);
            timestamp_task = itemView.findViewById(R.id.timestamp_task);
        }
    }

    public interface onClickListener {

        void onCheckClick(Task task, int pos, boolean isCheck);
        void onItemClick(Task task, int pos);

    }

}
