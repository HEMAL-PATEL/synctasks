package com.broooapps.synctasks.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.broooapps.synctasks.TaskListViewModel;
import com.broooapps.synctasks.fragments.ActiveTasksFragment;
import com.broooapps.synctasks.fragments.EditBottomSheetFragment;
import com.broooapps.synctasks.R;
import com.broooapps.synctasks.adapters.TasksListAdapter;
import com.broooapps.synctasks.helpers.Navigator;
import com.broooapps.synctasks.helpers.SharedPrefs;
import com.broooapps.synctasks.helpers.Util;
import com.broooapps.synctasks.models.datamodel.Task;
import com.firebase.client.Firebase;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.material.navigation.NavigationView.*;

public class TasksListActivity extends ProjectActivity implements View.OnClickListener {

    private String TAG = TasksListActivity.class.getSimpleName();
    private boolean addLayoutVisibility = false;

    public static final String DRAWER_ITEM_FRAGMENT_TAG = "DRAWER_ITEM_FRAGMENT_TAG";

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
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        Firebase.setAndroidContext(this);

        initializeViews();
        defaultConfigurations();
        setEventForViews();
    }

    @Override
    protected void initializeViews() {
        appBarLayout = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(android.R.drawable.ic_menu_edit);
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case android.R.id.home:
                                mDrawerLayout.openDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_active_tasks:
                                replaceContentFragment(new ActiveTasksFragment(), R.id.nav_active_tasks);
                                break;
                            case R.id.nav_logout:
                                FirebaseAuth.getInstance().signOut();
                                Navigator.openLoginActivity(getApplicationContext());
                                finish();
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });

    }

    @Override
    protected void setEventForViews() {
        navigationView.getHeaderView(0).findViewById(R.id.nav_header).setOnClickListener(this);

    }

    @Override
    protected void defaultConfigurations() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        replaceContentFragment(new ActiveTasksFragment(), R.id.nav_active_tasks);

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.text_user_id_header)).setText(user.getDisplayName());

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add: {
                if (!addLayoutVisibility) {
                    linear_new_task.setVisibility(View.VISIBLE);
                    Util.showKeyboard(this, input_new_task);
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

    public void replaceContentFragment(Fragment fragment, int res_id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        currentFragment = fragment;

        tx.replace(R.id.content_frame, fragment, DRAWER_ITEM_FRAGMENT_TAG);
        tx.commitAllowingStateLoss();
        navigationView.setCheckedItem(res_id);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
