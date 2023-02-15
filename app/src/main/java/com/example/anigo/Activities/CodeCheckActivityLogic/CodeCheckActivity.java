package com.example.anigo.Activities.CodeCheckActivityLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.anigo.DialogHelper.CreateErrorContactDialog;
import com.example.anigo.DialogHelper.CreateLoadingContactDialog;
import com.example.anigo.Activities.PasswordChangeActivityLogic.PasswordChangeActivity;
import com.example.anigo.R;

public class CodeCheckActivity extends AppCompatActivity implements CodeCheckActivityContract.View{

    private String code;

    private String email;

    private String current_code;

    private EditText code_tb;

    private Button code_check_button;

    CreateErrorContactDialog error_dialog;
    CreateLoadingContactDialog loading_dialog;


    private CodeCheckActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_check);

        Bundle bundle = getIntent().getExtras();

        code = bundle.getString("code");
        email = bundle.getString("email");

        code_check_button = findViewById(R.id.button_check_code);
        code_tb = findViewById(R.id.code_tb);

        presenter = new CodeCheckActivityPresenter(this, getApplicationContext());
        error_dialog = new CreateErrorContactDialog(this);
        loading_dialog = new CreateLoadingContactDialog(this);

        code_check_button.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                current_code = code_tb.getText().toString();
                if(!current_code.isEmpty()){
                    loading_dialog.ShowDialog();
                    presenter.CheckCode(code, current_code);
                }
                else {
                    error_dialog.CreateNewDialog("Поле пустое!");
                    error_dialog.ShowDialog();
                }
            }
        });


    }

    @Override
    public void OnSuccess(String code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_dialog.DeleteDialog();
                Intent intent = new Intent(CodeCheckActivity.this, PasswordChangeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

                
            }
        });
    }
    @Override
    public void OnError(String error_msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    loading_dialog.DeleteDialog();
                }catch (Exception ex){

                }
                error_dialog.CreateNewDialog(error_msg);
                error_dialog.ShowDialog();
            }
        });
    }
}