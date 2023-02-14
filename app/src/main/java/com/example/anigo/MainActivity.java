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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    public static View view;
    private static final String USER_LOGIN_CHECK = "NONE";
    private static final String USER_PASSWORD_CHECK = "NONE";

    private MainActivityContract.Presenter presenter;
    private CreateLoadingContactDialog loading_dialog;
    private CreateErrorContactDialog error_dialog;
    private TextView password_tv;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        loading_dialog = new CreateLoadingContactDialog(this);
        error_dialog = new CreateErrorContactDialog(this);

        TextView register_TV = findViewById(R.id.registerTV);
        Button loginbtn = findViewById(R.id.loginBTN);
        EditText login_tb = findViewById(R.id.loginTB);
        EditText register_tb = findViewById(R.id.regTB);

        context = getApplicationContext();
        password_tv = findViewById(R.id.password_change_tv);

        password_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(MainActivity.this, CodeSendActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.log_layout);



        System.out.println(login_tb.getText().toString());

        presenter = new MainActivityPresenter(this, getApplicationContext());



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

                    loading_dialog.ShowDialog();
                    if(login_tb_text.isEmpty() || register_tb_text.isEmpty()){
                        onError("Заполните все поля.");
                        return;
                    }else{
                        presenter.Login(login_tb_text, register_tb_text, getApplicationContext());
                    }
            }
        });



    }

    @Override
    public void onSuccess(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),NavigationActivity.class);
                startActivity(i);
                finish();
                loading_dialog.DeleteDialog();

            }
        });

    }

    @Override
    public void onError(String message, String body) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_dialog.DeleteDialog();
                error_dialog.CreateNewDialog(message);
                error_dialog.ShowDialog();
            }
        });
    }

    @Override
    public void onError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 loading_dialog.DeleteDialog();
                error_dialog.CreateNewDialog(message);
                error_dialog.ShowDialog();
            }
        });
    }

}