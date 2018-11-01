package com.broooapps.synctasks.activities;


import androidx.appcompat.app.AppCompatActivity;

public abstract class ProjectActivity extends AppCompatActivity {

    protected abstract void initializeViews();

    protected abstract void setEventForViews();

    protected abstract void defaultConfigurations();
}
