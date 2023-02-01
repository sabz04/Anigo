package com.example.anigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements RegisterActivityContract.View{

    RegisterActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_button = (Button) findViewById(R.id.registerBTN);

        EditText login_tb = findViewById(R.id.loginTB);
        EditText email_tb = findViewById(R.id.emailTB);
        EditText password_tb = findViewById(R.id.passwordTB);

        TextView login_text_view = findViewById(R.id.login_text_view);

        presenter = new RegisterActivityPresenter(this);

        login_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_tb_text = login_tb.getText().toString();
                String password_tb_text = password_tb.getText().toString();
                String email_tb_text = email_tb.getText().toString();

                if(TextUtils.isEmpty(login_tb_text) || TextUtils.isEmpty(password_tb_text) || TextUtils.isEmpty(email_tb_text)){

                    onError("Заполните все поля.");
                }else{
                    if(!email_tb_text.contains("@")){onError("Извините, но такой формат почты недействителен."); return;}
                    presenter.Register(password_tb_text, login_tb_text, email_tb_text, 2);
                }
            }
        });
    }

    @Override
    public void onSuccess(String message) {
        Context context = getWindow().getCurrentFocus().getContext();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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