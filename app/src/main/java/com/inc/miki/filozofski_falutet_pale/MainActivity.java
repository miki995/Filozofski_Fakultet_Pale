package com.inc.miki.filozofski_falutet_pale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homePage);

    }
    public void GoToSchedules(View v)
    {
        startActivity(new Intent(this,SchedulesActivity.class));
    }
}
