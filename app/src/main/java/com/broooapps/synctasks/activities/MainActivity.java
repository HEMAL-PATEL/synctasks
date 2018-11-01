package com.broooapps.synctasks.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.broooapps.synctasks.R;
import com.broooapps.synctasks.helpers.Navigator;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends ProjectActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setEventForViews();
        defaultConfigurations();

    }

    @Override
    protected void initializeViews() {

    }

    @Override
    protected void setEventForViews() {

    }

    @Override
    protected void defaultConfigurations() {

        InitializeApplication initate = new InitializeApplication(this);
        initate.execute();
    }

    private static class InitializeApplication extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> mContext;

        private InitializeApplication(Context mContext) {
            this.mContext = new WeakReference<>(mContext);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                init();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Navigator.openLoginActivity(mContext.get());
            ((MainActivity)mContext.get()).finish();
        }

        private void init() throws InterruptedException {
            Thread.sleep(2000);
        }
    }
}
