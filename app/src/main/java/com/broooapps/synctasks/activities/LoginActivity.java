package com.broooapps.synctasks.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.broooapps.synctasks.R;
import com.broooapps.synctasks.helpers.Navigator;
import com.broooapps.synctasks.helpers.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;

public class LoginActivity extends ProjectActivity implements View.OnClickListener {
    //
    private FirebaseAuth mAuth;
    private final String TAG = LoginActivity.class.getSimpleName();

    // view objects
    TextInputLayout input_layout_user_name, input_layout_user_email, input_layout_user_id, input_layout_password;
    EditText input_user_name, input_user_email, input_user_id, input_password;
    Button btn_login, btn_register;
    ProgressBar progress_bar;

    private boolean isRegisterView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setEventForViews();
        defaultConfigurations();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Util.hideKeyboardFrom(this, input_password);
                loginOrSignupUser();
                break;
            case R.id.btn_register:
                toggleRegisterView();
                break;
        }

    }

    @Override
    protected void initializeViews() {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        input_layout_password = findViewById(R.id.input_layout_password);
        input_layout_user_name = findViewById(R.id.input_layout_user_name);
        input_layout_user_email = findViewById(R.id.input_layout_user_email);
        input_layout_user_id = findViewById(R.id.input_layout_user_id);

        input_user_name = findViewById(R.id.input_user_name);
        input_user_email = findViewById(R.id.input_user_email);
        input_user_id = findViewById(R.id.input_user_id);
        input_password = findViewById(R.id.input_password);

        progress_bar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void setEventForViews() {
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    protected void defaultConfigurations() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userLogin(currentUser);

        input_user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Util.isValidEmail(charSequence.toString())) {
                    input_user_email.setError("Please enter a valid e-mail");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() < 8) {
                    input_password.setError("Password should be " + (8 - charSequence.toString().length()) + " characters long");
                } else {
                    input_password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // class methods


    private void loginOrSignupUser() {

        progress_bar.setVisibility(View.VISIBLE);

        String email = input_user_email.getText().toString();
        String password = input_password.getText().toString();
        String name = input_user_name.getText().toString();

        if (email.equals("")) email = input_user_id.getText().toString();

        if (Util.isValidEmail(email)) {
            if (Util.isValidPassword(password)) {
                if (isRegisterView) signUpUser(email, password, name);
                else signInUser(email, password);
            }
        } else {
            progress_bar.setVisibility(View.GONE);
        }
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                            userLogin(null);
                        }

                        // ...
                    }
                });
    }

    private void signUpUser(String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            if (user != null) {

                                user.updateProfile(request)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) userLogin(user);

                                            }
                                        });

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                            userLogin(null);
                        }

                        // ...
                    }
                });

    }


    private void userLogin(FirebaseUser user) {
        if (user == null) return;
        else {
            Navigator.openTasksListActivity(this);
            finish();
        }

    }

    private void toggleRegisterView() {
        if (!isRegisterView) {

            isRegisterView = true;

            input_layout_user_name.setVisibility(View.VISIBLE);
            input_layout_user_email.setVisibility(View.VISIBLE);
            btn_login.setText(getResources().getString(R.string.btn_register));
            btn_register.setText(R.string.btn_register_toggle);
        } else {

            isRegisterView = false;

            input_layout_user_email.setVisibility(View.GONE);
            input_layout_user_name.setVisibility(View.GONE);
            btn_login.setText(getResources().getText(R.string.btn_login));
            btn_register.setText(getResources().getText(R.string.btn_register));
        }
    }
}
