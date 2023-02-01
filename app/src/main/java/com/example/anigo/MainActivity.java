package com.example.anigo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anigo.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    public static View view;
    private static final String USER_LOGIN_CHECK = "NONE";
    private static final String USER_PASSWORD_CHECK = "NONE";
    MainActivityContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView register_TV = findViewById(R.id.registerTV);
        Button loginbtn = findViewById(R.id.loginBTN);
        EditText login_tb = findViewById(R.id.loginTB);
        EditText register_tb = findViewById(R.id.regTB);

        LinearLayout layout = (LinearLayout) findViewById(R.id.log_layout);

        layout.setVisibility(View.INVISIBLE);

        System.out.println(login_tb.getText().toString());

        presenter = new MainActivityPresenter(this);

        presenter.Login(USER_LOGIN_CHECK, USER_PASSWORD_CHECK, this );

        register_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String login_tb_text = login_tb.getText().toString();
                    String register_tb_text = register_tb.getText().toString();

                    if(TextUtils.isEmpty(login_tb_text) || TextUtils.isEmpty(register_tb_text)){
                        onError("Заполните все поля.");
                    }else{
                        presenter.Login(login_tb_text, register_tb_text, getApplicationContext());
                    }
            }
        });



    }

    @Override
    public void onSuccess(String message) {

        Context context = getApplicationContext();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),NavigationActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onError(String message, String body) {
        Context context = getWindow().getCurrentFocus().getContext();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onError(String message) {
        Context context = getWindow().getCurrentFocus().getContext();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}