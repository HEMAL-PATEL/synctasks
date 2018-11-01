package com.broooapps.synctasks.helpers;

import android.content.Context;
import android.content.Intent;

import com.broooapps.synctasks.activities.LoginActivity;
import com.broooapps.synctasks.activities.TasksListActivity;

public class Navigator {

    public static void openLoginActivity(Context context) {

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    public static void openTasksListActivity(Context context) {
        Intent intent = new Intent(context, TasksListActivity.class);
        context.startActivity(intent);
    }
}
