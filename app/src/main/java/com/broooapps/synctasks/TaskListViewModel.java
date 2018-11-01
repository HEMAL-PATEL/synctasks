package com.broooapps.synctasks;

import android.provider.ContactsContract;

import com.broooapps.synctasks.helpers.Constants;
import com.broooapps.synctasks.models.datamodel.Task;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TaskListViewModel extends ViewModel {
    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;
    private FirebaseQueryLiveData liveData;


    public LiveData<ArrayList<Task>> getTaskList() {
        if (liveData == null) {
            liveData = new FirebaseQueryLiveData(new Firebase(Constants.db_url + userID));
        }
        return liveData;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        liveData.setTaskList(taskList);
    }
}
