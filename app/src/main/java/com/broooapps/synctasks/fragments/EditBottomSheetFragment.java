package com.broooapps.synctasks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.broooapps.synctasks.R;
import com.broooapps.synctasks.models.datamodel.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditBottomSheetFragment extends BottomSheetDialogFragment {

    // view objects
    EditText edit_task;
    Button btn_confirm_edit;
    ImageView image_delete;


    //listeners
    IEditTask listener;

    Task task;

    public EditBottomSheetFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edit_task = view.findViewById(R.id.input_edit_task);
        image_delete = view.findViewById(R.id.image_delete);
        btn_confirm_edit = view.findViewById(R.id.btn_confirm_edit);

        edit_task.setText(task.getTask_name());

        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClicked(task);
            }
        });

        btn_confirm_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setTask_name(edit_task.getText().toString());
                listener.onTaskEdited(task);
            }
        });
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setListener(IEditTask listener) {
        this.listener = listener;
    }

    public interface IEditTask {

        void onDeleteClicked(Task t);
        void onTaskEdited(Task t);
    }
}
