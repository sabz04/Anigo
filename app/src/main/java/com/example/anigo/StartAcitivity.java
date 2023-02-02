package com.example.anigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.HardwareBuffer;
import android.os.Bundle;
import android.os.Handler;

public class StartAcitivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_acitivity);


        Handler hnd = new Handler();

        hnd.post(new Runnable() {
            @Override
            public void run() {
                FeedUserDbHelper db = new FeedUserDbHelper(getApplicationContext());


                FeedUserLocal user_local = db.CheckIfExist();



                if(user_local == null)
                    intent = new Intent(StartAcitivity.this, MainActivity.class);
                else
                    intent = new Intent(StartAcitivity.this, NavigationActivity.class);

                startActivity(intent);
                finish();
            }
        });

    }
}