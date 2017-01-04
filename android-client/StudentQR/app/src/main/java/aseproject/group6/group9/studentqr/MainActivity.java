package aseproject.group6.group9.studentqr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by Florian Schulz on 13.12.2016.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHARED_PREFERENCES_KEY = "QR_SHARED";
    public static final String QR_CODE_PREFERENCES_KEY = "qrCodeString";
    public static final String QR_CODE_STATUS_STRING_KEY = "qrCodeStatusString";
    private static final String TAG = "LOGIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreQrCode();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fetchQrCodeButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.message_qrCodeFetching,
                        Toast.LENGTH_SHORT).show();
                doRestCallAsync();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
                    navigationViewUser.setText(user.getDisplayName());
                    navigationViewEmail.setText(user.getEmail());
                } else {
                    // User is signed out
                    navigationViewUser.setText(R.string.nav_account_name);
                    navigationViewEmail.setText(R.string.nav_user_email);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void restoreQrCode() {
        // Restore shared preferences
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
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
            qrCodeStatus.setText("Fetched");

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        preferences.edit().remove(QR_CODE_PREFERENCES_KEY).commit();
    }

    private void doRestCallAsync() {
        String url = "http://www.thomas-bayer.com/sqlrest/CUSTOMER/3/";
        try {
            new DownloadQRCodeTask(MainActivity.this).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // switching to Account Activity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "change");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            // Handle the action
        } else if (id == R.id.nav_manage) {
            // Handle the action
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
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private class DownloadQRCodeTask extends AsyncTask<URL, Integer, String> {

        private Activity activity;

        DownloadQRCodeTask(Activity activity) {
            this.activity = activity;
        }

        protected String doInBackground(URL... urls) {
            String rawAnswer = null;
            try {
                rawAnswer = new ClientResource(urls[0].toString()).get().getText();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResourceException e) {
                // TODO better log
                Log.d("e", "404 or something");
            }
            return rawAnswer;
        }

        protected void onPostExecute(String result) {

            ByteArrayInputStream is;
            String content = "";
            try {
                is = new ByteArrayInputStream(result.getBytes("UTF-8"));

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(is);

                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList nodes = (NodeList) xpath.evaluate("//*/LASTNAME/text()", doc, XPathConstants.NODESET);
                int length = nodes.getLength();
                for (int i = 0; i < length; i++) {
                    //content += "\n" + (i + 1) + ". " + nodes.item(i).getNodeValue();
                    content += nodes.item(i).getNodeValue();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            addQrSharedPreferences(content);
            generateQRCodeImage(content);
        }
    }

    private void addQrSharedPreferences(String qrCodeString) {
        /* Shared Preferences */
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(QR_CODE_PREFERENCES_KEY, qrCodeString);
        // TODO Put the String inside
        editor.putString(QR_CODE_STATUS_STRING_KEY, "Done");
        editor.commit();
    }


}
