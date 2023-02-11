package com.example.anigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.HardwareBuffer;
import android.os.Bundle;
import android.os.Handler;

public class StartAcitivity extends AppCompatActivity implements AuthentificationInterface.Listener{

    private Intent intent;

    private Authentification auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_acitivity);
        auth = new Authentification(this, getApplicationContext());
        auth.Auth();


    }

    @Override
    public void AuthSuccess(String message) {

        intent = new Intent(StartAcitivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void AuthError(String message) {
        intent = new Intent(StartAcitivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void AuthSuccess(String token, int user_id) {

    }
}