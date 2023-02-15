package com.example.anigo.Activities.CodeSendActivityLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anigo.Activities.CodeCheckActivityLogic.CodeCheckActivity;
import com.example.anigo.DialogHelper.CreateErrorContactDialog;
import com.example.anigo.DialogHelper.CreateLoadingContactDialog;
import com.example.anigo.R;

public class CodeSendActivity extends AppCompatActivity implements CodeSendActivityContract.View {
    CodeSendActivityPresenter presenter;

    private Button btn_send;
    private Button btn_back;
    private String email = "";
    private EditText email_edit;

    CreateLoadingContactDialog loading_dial;
    CreateErrorContactDialog error_dial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_send);

        presenter = new CodeSendActivityPresenter(this, getApplicationContext());
        error_dial = new CreateErrorContactDialog(this);
        loading_dial = new CreateLoadingContactDialog(this);

        Bundle bundle = getIntent().getExtras();

        email_edit =  findViewById(R.id.email_tb);
        //try_catch needs to advanced usage, f.e. when user not logged on in the system
        try {
            email = bundle.getString("email");
            if(!email.isEmpty()){
                email_edit.setText(email);
            }
        }
        catch (Exception ex){
            email = "";
        }

        btn_send = findViewById(R.id.button_get_code);

        btn_back = findViewById(R.id.back_btn);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = email_edit.getText().toString();
                if(!email.isEmpty()){
                    presenter.GetCode(email);
                    loading_dial.ShowDialog();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            error_dial.CreateNewDialog("Поле почты не должно оставаться пустым!");
                            error_dial.ShowDialog();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void OnSuccess(String code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CodeSendActivity.this, CodeCheckActivity.class);

                Bundle bundle_put_code = new Bundle();

                bundle_put_code.putString("code", code);

                bundle_put_code.putString("email", email);

                intent.putExtras(bundle_put_code);
                loading_dial.DeleteDialog();
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
                loading_dial.DeleteDialog();
                error_dial.CreateNewDialog(error_msg);
                error_dial.ShowDialog();
            }
        });
    }
}