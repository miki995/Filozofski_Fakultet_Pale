package com.inc.miki.filozofski_falutet_pale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;



public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    int pStatus = 0;
    TextView tv;
    private Handler handler = new Handler();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public String HOST_STRING = "";
    public String WEBSITE_STRING = "";

    private static final String HOST_URL = "host";
    private static final String WEBSITE_URL = "website_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocetna);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        mWebView = (WebView) findViewById(R.id.mwebview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        /*For FireBase

         */

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        fetchFromFireBase();
        HOST_STRING = mFirebaseRemoteConfig.getString(HOST_URL);
        WEBSITE_STRING = mFirebaseRemoteConfig.getString(WEBSITE_URL);


        tv = (TextView) findViewById(R.id.tv);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 100) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(pStatus);
                            tv.setText(pStatus + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(8); //thread will take approx 1.5 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        if(isConnectingToInternet(MainActivity.this)){
            mWebView.loadUrl(WEBSITE_STRING);
        }else{
            setContentView(R.layout.greska);
        }


        mWebView.setWebViewClient(new MyAppWebViewClient());

    }

    private void fetchFromFireBase() {


        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Uspjesno, ucitavam raspored!" ,
                                    Toast.LENGTH_LONG).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "Neuspjesno povlacenje sa interneta!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager)mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }



    @Override
    public void onBackPressed(){
        if(mWebView.canGoBack()){mWebView.goBack();}
        else {super.onBackPressed();}



    }
    public void Reload(View v) {
        fetchFromFireBase();
        HOST_STRING = mFirebaseRemoteConfig.getString(HOST_URL);
        WEBSITE_STRING = mFirebaseRemoteConfig.getString(WEBSITE_URL);

        MainActivity.this.mWebView.loadUrl(WEBSITE_STRING);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh:
                MainActivity.this.mWebView.loadUrl(WEBSITE_STRING);

                invalidateOptionsMenu(); // This works on Android 3.x devices only
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private class  MyAppWebViewClient extends WebViewClient {}

}
