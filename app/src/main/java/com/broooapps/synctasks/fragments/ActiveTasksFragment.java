package com.broooapps.synctasks.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.broooapps.synctasks.R;
import com.broooapps.synctasks.TaskListViewModel;
import com.broooapps.synctasks.activities.TasksListActivity;
import com.broooapps.synctasks.adapters.TasksListAdapter;
import com.broooapps.synctasks.helpers.Navigator;
import com.broooapps.synctasks.helpers.SharedPrefs;
import com.broooapps.synctasks.helpers.Util;
import com.broooapps.synctasks.models.datamodel.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActiveTasksFragment extends Fragment implements View.OnClickListener, EditBottomSheetFragment.IEditTask, TasksListAdapter.onClickListener {
    private String TAG = ActiveTasksFragment.class.getSimpleName();
    private boolean addLayoutVisibility = false;

    private ArrayList<Task> tasksList;
    private TasksListAdapter adapter;

    private FirebaseUser user;

    private TaskListViewModel viewModel;

    private Animation rotate_forward, rotate_backward;
    private AppBarLayout appBarLayout;
    private DrawerLayout mDrawerLayout;
    private CheckBox check_new_task;
    private EditText input_new_task;
    private EditBottomSheetFragment editBottomSheet;
    private FloatingActionButton fab_add;
    private NavigationView navigationView;
    private LinearLayout linear_new_task;
    private ProgressBar progressBar;
    private RecyclerView recycler_list;
    private Toolbar toolbar;

    public ActiveTasksFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeViews();
        defaultConfigurations();
        setEventForViews();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tasks_list, container, false);

    }

    private void initializeViews() {
        check_new_task = getActivity().findViewById(R.id.check_new_task);
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        fab_add = getActivity().findViewById(R.id.fab_add);
        input_new_task = getActivity().findViewById(R.id.input_new_task);
        linear_new_task = getActivity().findViewById(R.id.linear_new_task);
        progressBar = getActivity().findViewById(R.id.progress);
        recycler_list = getActivity().findViewById(R.id.recycler_list);


        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
    }

    protected void setEventForViews() {
        fab_add.setOnClickListener(this);

    }

    protected void defaultConfigurations() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        progressBar.setVisibility(View.GONE);
        viewModelConfigurations();

        recyclerViewConfigurations();

        handleEnterPressed();
    }

    private void viewModelConfigurations() {
        viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
        viewModel.setUserID(user.getUid());
        LiveData<ArrayList<Task>> listLiveData = viewModel.getTaskList();

        listLiveData.observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(ArrayList<Task> tasks) {
                try {
                    tasksList = tasks;
                    adapter.updateData(tasks);
                    progressBar.setVisibility(View.GONE);
                } catch (Exception ignored) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void handleEnterPressed() {
        input_new_task.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {
                    addNewTask();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void addNewTask() {

        String task_name = input_new_task.getText().toString();
        String timestamp = Util.getCurrentDate();

        Task newTask = new Task(task_name, timestamp, check_new_task.isChecked());

        tasksList.add(0, newTask);

        SharedPrefs.setValue(getContext(), SharedPrefs.PREF_TASKS, new Gson().toJson(tasksList));

        adapter.updateData(tasksList);
        hideNewTaskLayout();

        updateFirebaseData();
    }

    private void updateFirebaseData() {
        viewModel.setTaskList(tasksList);
    }

    private void hideNewTaskLayout() {
        addLayoutVisibility = false;
        Util.hideKeyboardFrom(getContext(), input_new_task);
        input_new_task.setText("");
        linear_new_task.setVisibility(View.GONE);
        fab_add.startAnimation(rotate_backward);
    }

    private void recyclerViewConfigurations() {

        if (tasksList == null) {
            tasksList = new Gson().fromJson(SharedPrefs.getValue(getContext(),
                    SharedPrefs.PREF_TASKS, ""),
                    new TypeToken<ArrayList<Task>>() {
                    }.getType());
        }

        adapter = new TasksListAdapter(tasksList, this);
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_list.setAdapter(adapter);
    }

    private void updateTaskList() {
        adapter.updateData(tasksList);
        updateFirebaseData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add: {
                if (!addLayoutVisibility) {
                    linear_new_task.setVisibility(View.VISIBLE);
                    Util.showKeyboard(getContext(), input_new_task);
                    addLayoutVisibility = true;
                    fab_add.startAnimation(rotate_forward);
                } else {
                    linear_new_task.setVisibility(View.GONE);
                    addLayoutVisibility = false;
                    fab_add.startAnimation(rotate_backward);
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onCheckClick(Task task, int pos, boolean b) {
        for (Task t : tasksList) {
            if (t.equals(task))
                t.setStatus(b);
        }
        updateFirebaseData();
    }

    @Override
    public void onItemClick(Task task, int pos) {
        editBottomSheet = new EditBottomSheetFragment();
        editBottomSheet.setListener(this);
        editBottomSheet.show(getActivity().getSupportFragmentManager(), editBottomSheet.getTag());
        editBottomSheet.setTask(task);
    }

    @Override
    public void onDeleteClicked(Task t) {
        int pos = -1;
        int counter = 0;
        for (Task task : tasksList) {
            if (task.equals(t)) pos = counter;
            counter++;
        }
        if (counter != 0) {
            tasksList.remove(pos);
        }
        updateTaskList();
        editBottomSheet.dismiss();
    }

    @Override
    public void onTaskEdited(Task t) {
        for (Task task : tasksList) {
            if (task.equals(t)) {
                task.setTask_name(t.getTask_name());
                break;
            }
        }
        updateTaskList();
        editBottomSheet.dismiss();
    }

}
