package aseproject.group6.group9.studentqr;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Florian Schulz on 13.12.2016.
 */

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private GoogleApiClient client;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LOGIN";
    private EditText passwordView;
    private EditText emailView;
    private Button loginButtonView;
    private TextView loginStatusView;
    private Button logoutButtonView;
    public static final String PREF_REST_TOKEN = "restToken";

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO remove after checking the json stuff works
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_login_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // TODO Implement the layout better (three points and stuff)

        passwordView = (EditText) findViewById(R.id.login_password);
        emailView = (EditText) findViewById(R.id.login_email);
        loginButtonView = (Button) findViewById(R.id.login_button);
        loginStatusView = (TextView) findViewById(R.id.login_status_text);
        logoutButtonView = (Button) findViewById(R.id.logout_button);

        loginButtonView.setOnClickListener(this);
        logoutButtonView.setOnClickListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                    navigationViewUser.setText(user.getDisplayName());
                    navigationViewEmail.setText(user.getEmail());
                } else {
                    // User is signed out
                    navigationViewUser.setText(R.string.nav_account_name);
                    navigationViewEmail.setText(R.string.nav_user_email);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_button) {
            signIn(emailView.getText().toString(), passwordView.getText().toString());
        } else if (i == R.id.logout_button) {
            signOut();
        }
    }

    private void fetchRestAuthToken(String email, String password) {
        // TODO, what if, credentials are wrong...maybe on continue, after sign in

        // send request to server

        // get request from server

        String urlString = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDW2PaX5m9MwqGDKK-mSLFFfCscbJfF5ek";
        try{
            JSONObject jsonObject = postJSONObjectToURL(urlString, email, password);
            String jsonObjectString = jsonObject.toString();

            System.out.println("JSON OBJECT");
            Log.d("myLocalIDis: ", jsonObject.getString("localID"));
            Log.d("myRestTokenis: ", jsonObject.getString("idToken"));
            System.out.println("JSON OBJECT");
            // TODO Parse JSON

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    public static JSONObject postJSONObjectToURL(String urlString, String email, String password) throws IOException, JSONException {

        URL url = new URL(urlString);

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        //urlConnection.setDoInput(true);
        urlConnection.connect();

        JSONObject request = new JSONObject();

        request.put("email", email);
        request.put("password", password);
        request.put("returnSecureToken", "true");

        Log.d("REQUEST_URL", request.toString());
        Charset charset = Charset.forName("UTF8");

        OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream(), charset);
        wr.write(request.toString());
        wr.close();


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
            System.out.println("" + sb.toString());
        } else if(HttpResult == 400) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getErrorStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            Log.d("RESULT: ", sb.toString());
            System.out.println("" + sb.toString());
        } else {
            System.out.println(urlConnection.getResponseMessage());
        }

        return new JSONObject(sb.toString());
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // TODO implement better
        fetchRestAuthToken(email, password);


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            // TODO handling if login task is not successful
                            //loginStatusView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]

    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.auth_required));
            valid = false;
        } else {
            emailView.setError(null);
        }

        String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.auth_required));
            valid = false;
        } else {
            passwordView.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            loginStatusView.setText(getString(R.string.login_status_text, user.getEmail()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            loginButtonView.setVisibility(View.GONE);
            emailView.setVisibility(View.GONE);
            passwordView.setVisibility(View.GONE);

            loginStatusView.setVisibility(View.VISIBLE);
            logoutButtonView.setVisibility(View.VISIBLE);

        } else {
            loginStatusView.setText(null);
            //mDetailTextView.setText(null)

            loginButtonView.setVisibility(View.VISIBLE);
            emailView.setVisibility(View.VISIBLE);
            passwordView.setVisibility(View.VISIBLE);

            loginStatusView.setVisibility(View.GONE);
            logoutButtonView.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

        } else if (id == R.id.nav_slideshow) {
            // Handle the action
        } else if (id == R.id.nav_manage) {
            // Handle the action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_login_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_login_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.progress_loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
