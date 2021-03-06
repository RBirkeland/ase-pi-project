package aseproject.group6.group9.studentqr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by Florian Schulz on 13.12.2016.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences settings;
    public static final String QR_CODE_PREFERENCES_KEY = "qrCodeString";
    public static final String QR_CODE_STATUS_STRING_KEY = "qrCodeStatusString";
    public static final String QR_CODE_SPINNER_STRING_KEY = "qrCodeSpinnerString";
    private static final String TAG = "LOGIN";
    private static final String FIREBASE_REST_URL = "https://ase-pi-project.firebaseio.com";
    private static final String USER_TOKEN = "userToken";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Spinner spinner;
    NavigationView navigationView;


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        restoreQrCode();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fetchQrCodeButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRestCallAsync();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        spinner = (Spinner) findViewById(R.id.qrCodeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = settings.edit();
                editor.putString(QR_CODE_SPINNER_STRING_KEY, parent.getItemAtPosition(position).toString());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                TextView navigationViewUser = (TextView) findViewById(R.id.nav_account_name);
                TextView navigationViewEmail = (TextView) findViewById(R.id.nav_user_email);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // TODO show them and search for nullpointer...
                    //navigationViewUser.setText("");
                    //navigationViewEmail.setText(user.getEmail());
                } else {
                    // User is signed out
                    //navigationViewUser.setText(R.string.nav_account_name);
                    //navigationViewEmail.setText(R.string.nav_user_email);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        recreateUserToken();
    }

    private void restoreQrCode() {
        // Restore shared preferences
        String qrCodeString = settings.getString(QR_CODE_PREFERENCES_KEY, null);
        String qrCodeStatusString = settings.getString(QR_CODE_STATUS_STRING_KEY, null);

        if (qrCodeString != null) {
            generateQRCodeImage(qrCodeString);
            TextView qrCodeStatusTextView = (TextView) findViewById(R.id.qrCodeStatusString);
            qrCodeStatusTextView.setText(qrCodeStatusString);
        }
    }

    public void generateQRCodeImage(String qrCodeString){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeString, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView qrCodeImage = (ImageView) this.findViewById(R.id.qrCode);
            qrCodeImage.setImageBitmap(bitmap);
            qrCodeImage.setVisibility(View.VISIBLE);
            TextView qrCodeStatus = (TextView) this.findViewById(R.id.qrCodeStatusString);
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = s.format(new Date());
            addQrTimeSharedPreferences(format);
            qrCodeStatus.setText(format);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
            Log.d("e", "There was an unknown exception during QRCodeGeneration");
            Toast.makeText(MainActivity.this, R.string.err_msg_qr_fetching,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_fetch_qrCodeString) {
            doRestCallAsync();
            return true;
        } else if (id == R.id.action_remove_sharedPreferences) {
            removeQrSharedPreferences();
            ImageView qrCodeImage = (ImageView) findViewById(R.id.qrCode);
            qrCodeImage.setVisibility(View.INVISIBLE);
            TextView qrCodeStatus = (TextView) findViewById(R.id.qrCodeStatusString);
            qrCodeStatus.setText(R.string.please_fetch_qr);
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeQrSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().remove(QR_CODE_PREFERENCES_KEY).apply();
    }

    private void doRestCallAsync() {

        Toast.makeText(MainActivity.this, R.string.message_qrCodeFetching,
                Toast.LENGTH_SHORT).show();

        if(isLoginStatusValid()){
            try {
                new DownloadJSONQRCodeTask(MainActivity.this).execute();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this, R.string.message_qrCodeFetchingNoAccount,
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isLoginStatusValid() {
        if (mAuth.getCurrentUser() != null){
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // switching to Login Activity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "change");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        mAuth.addAuthStateListener(mAuthListener);
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void addQrSharedPreferences(String qrCodeString) {
        /* Shared Preferences */
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(QR_CODE_PREFERENCES_KEY, qrCodeString);
        editor.apply();
    }

    private void addQrTimeSharedPreferences(String time) {
        /* Shared Preferences */
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(QR_CODE_STATUS_STRING_KEY, time);
        editor.apply();
    }

    private class DownloadJSONQRCodeTask extends AsyncTask<Void, Integer, String> {

        private Activity activity;

        DownloadJSONQRCodeTask(Activity activity) {
            this.activity = activity;
        }

        protected String doInBackground(Void... arg0) {
            String rawAnswer = null;
            try {
                rawAnswer = getQRTokenForWeek();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("e", "404 or something");
            }
            return rawAnswer;
        }

        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, R.string.message_qrCodeFetchingSuccess,
                    Toast.LENGTH_SHORT).show();
            addQrSharedPreferences(result);
            generateQRCodeImage(result);
        }
    }

    public String getQRTokenForWeek(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            final String userUid = mAuth.getCurrentUser().getUid();
            String url = FIREBASE_REST_URL + "/user/" + userUid +"/week/"+spinner.getSelectedItem().toString()+".json?auth="+settings.getString(USER_TOKEN, null);
            try{
                JSONObject jsonObject = getJSONObjectFromURL(url);
                return jsonObject.getString("token");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
        return null;
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        URL url = new URL(urlString);

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoInput(true);
        urlConnection.connect();

        StringBuilder sb = new StringBuilder();
        int HttpResult = urlConnection.getResponseCode();
        Log.d("RESPONSE MESSAGE IS: ", urlConnection.getResponseMessage());
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } else if(HttpResult == 400) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getErrorStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } else {
            System.out.println(urlConnection.getResponseMessage());
        }

        return new JSONObject(sb.toString());
    }

    private class DownloadUserTokenTask extends AsyncTask<Void, Integer, String> {

        private Activity activity;

        DownloadUserTokenTask(Activity activity) {
            this.activity = activity;
        }

        protected String doInBackground(Void... arg0) {
            String rawAnswer = null;
            try {
                rawAnswer =
                        getUserToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rawAnswer;
        }

        protected void onPostExecute(String result) {
            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_TOKEN, result);
            editor.apply();
        }
    }

    public String getUserToken(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            return user.getToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("JWT-Token: ", task.getResult().getToken());
                    }
                }
            }).getResult().getToken();
        }
        return null;
    }

    public void recreateUserToken(){
        if(isLoginStatusValid()){
            try {
                new DownloadUserTokenTask(MainActivity.this).execute();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this, R.string.message_qrCodeFetchingNoAccount,
                    Toast.LENGTH_LONG).show();
        }
    }

}
