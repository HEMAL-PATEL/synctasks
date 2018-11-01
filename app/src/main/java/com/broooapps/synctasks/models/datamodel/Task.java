package com.broooapps.synctasks.models.datamodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey
    private int id;
    private String task_name;
    private String timestamp;
    private boolean status;


    public Task(String task_name, String timestamp, boolean status) {
        this.task_name = task_name;
        this.timestamp = timestamp;
        this.status = status;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
