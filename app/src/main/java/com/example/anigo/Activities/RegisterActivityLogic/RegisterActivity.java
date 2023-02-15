package com.example.anigo.Activities.RegisterActivityLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anigo.DialogHelper.CreateErrorContactDialog;
import com.example.anigo.DialogHelper.CreateLoadingContactDialog;
import com.example.anigo.Activities.MainActivityLogic.MainActivity;
import com.example.anigo.R;

public class RegisterActivity extends AppCompatActivity implements RegisterActivityContract.View{

    RegisterActivityPresenter presenter;
    CreateLoadingContactDialog loadingContactDialog;
    CreateErrorContactDialog errorContactDialog;
    Context context;


    TextView login_tb;
    TextView email_tb;
    TextView password_tb;
    TextView login_text_view;

    Button register_button;

    String login_tb_text;
    String password_tb_text;
    String email_tb_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_button = findViewById(R.id.registerBTN);

        login_tb = findViewById(R.id.loginTB);
        email_tb = findViewById(R.id.emailTB);
        password_tb = findViewById(R.id.passwordTB);

        login_text_view = findViewById(R.id.login_text_view);

        context = getApplicationContext();
        presenter = new RegisterActivityPresenter(this);

        loadingContactDialog = new CreateLoadingContactDialog(this);
        errorContactDialog = new CreateErrorContactDialog(this);


        login_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_tb_text = login_tb.getText().toString();
                password_tb_text = password_tb.getText().toString();
                email_tb_text = email_tb.getText().toString();

                if(TextUtils.isEmpty(login_tb_text) || TextUtils.isEmpty(password_tb_text) || TextUtils.isEmpty(email_tb_text)){

                    onError("Заполните все поля.");
                }else{
                    if(!email_tb_text.contains("@")){

                        onError("Извините, но такой формат почты недействителен.");
                        return;
                    }
                    if(password_tb_text.length() < 6){
                        onError("Пароль должен содержать больше 5 символов.");
                        return;
                    }
                    loadingContactDialog.ShowDialog();
                    //sending request
                    presenter.Register(password_tb_text, login_tb_text, email_tb_text, 2);
                }
            }
        });
    }

    @Override
    public void onSuccess(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingContactDialog.DeleteDialog();
                Toast.makeText(RegisterActivity.this , "Пользователь зарегистрирован.", Toast.LENGTH_LONG).show();
                email_tb.setText("");
                password_tb.setText("");
                login_tb.setText("");
            }
        });

    }

    @Override
    public void onError(String message, String body) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingContactDialog.DeleteDialog();

                errorContactDialog.CreateNewDialog(message);
                errorContactDialog.ShowDialog();
            }
        });

    }

    @Override
    public void onError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingContactDialog.DeleteDialog();

                errorContactDialog.CreateNewDialog(message);
                errorContactDialog.ShowDialog();
            }
        });

    }
}