package aseproject.group6.group9.studentqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by Florian Schulz on 13.12.2016.
 */

public class LauncherActivity extends AppCompatActivity {

    public static final int LAUNCHER_TIME = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        new Timer().schedule(new TimerTask(){
            public void run() {
                LauncherActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                        // finishes LauncherActivity
                        finish();
                    }
                });
            }
        }, LAUNCHER_TIME);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
