package com.broooapps.synctasks;

import com.broooapps.synctasks.models.datamodel.Task;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;

public class FirebaseQueryLiveData extends LiveData<ArrayList<Task>> {

    private static final String TAG = FirebaseQueryLiveData.class.getSimpleName();

    private final Firebase reference;

    private final ChildEventListener listener = new ChildEventListener();
    private final MyValueEventListener listener1 = new MyValueEventListener();

    public FirebaseQueryLiveData(Firebase reference) {
        this.reference = reference;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("json", new Gson().toJson(taskList));
        reference.setValue(map);
    }

    @Override
    protected void onActive() {
        reference.child("json").addValueEventListener(listener1);
        reference.addChildEventListener(listener);
    }

    @Override
    protected void onInactive() {
        reference.child("json").removeEventListener(listener1);
        reference.removeEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                ArrayList<Task> list = new Gson().fromJson(dataSnapshot.getValue().toString(),
                        new TypeToken<ArrayList<Task>>() {
                        }.getType());
                setValue(list);
            } else {
                setValue(new ArrayList<Task>());
            }

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }


    private class ChildEventListener implements com.firebase.client.ChildEventListener {

        @Override
        public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
            ArrayList<Task> list = new Gson().fromJson(dataSnapshot.getValue().toString(),
                    new TypeToken<ArrayList<Task>>() {
                    }.getType());
            setValue(list);
        }

        @Override
        public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }


}
