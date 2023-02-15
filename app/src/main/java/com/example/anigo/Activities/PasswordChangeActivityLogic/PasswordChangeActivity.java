package com.example.anigo.Activities.PasswordChangeActivityLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anigo.DialogHelper.CreateErrorContactDialog;
import com.example.anigo.DialogHelper.CreateLoadingContactDialog;
import com.example.anigo.Activities.MainActivityLogic.MainActivity;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.R;

public class PasswordChangeActivity extends AppCompatActivity implements PasswordChangeActivityContract.View {


    private Button pass_change_btn;

    String email;

    CreateLoadingContactDialog loading_dialog;

    CreateErrorContactDialog error_dialog;

    private EditText pass_change_tv;

    private PasswordChangePresenter presenter;

    private String password_changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        email = getIntent().getExtras().getString("email");

        pass_change_tv = findViewById(R.id.password_tb);
        pass_change_btn = findViewById(R.id.button_pass_change);

        presenter = new PasswordChangePresenter(this, getApplicationContext());
        error_dialog = new CreateErrorContactDialog(this);
        loading_dialog = new CreateLoadingContactDialog(this);

        pass_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password_changed = pass_change_tv.getText().toString();
                if(!password_changed.isEmpty()){
                    presenter.ChangePass(password_changed, email);
                    loading_dialog.ShowDialog();
                }
                else{
                    error_dialog.CreateNewDialog("Поле не может быть пустым!");
                    error_dialog.ShowDialog();
                }
            }
        });

    }

    @Override
    public void OnSuccess(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_dialog.DeleteDialog();
            }
        });

        Intent intent = new Intent(PasswordChangeActivity.this, NavigationActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void OnError(String message) {
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
    public void OnSuccessNoLogon(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_dialog.DeleteDialog();
            }
        });
        Intent intent = new Intent(PasswordChangeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}